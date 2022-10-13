/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.cursor.defaults;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetWrapper;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is the default implementation of a MyBatis Cursor.
 * This implementation is not thread safe.
 *
 * @author Guillaume Darmont / guillaume@dropinocean.com
 */
public class DefaultCursor<T> implements Cursor<T> {

  // ResultSetHandler stuff

  /**
   * 对象包装结果处理类
   */
  private final DefaultResultSetHandler resultSetHandler;
  /**
   * 结果映射
   */
  private final ResultMap resultMap;
  /**
   * ResultSet 包装对象
   */
  private final ResultSetWrapper rsw;
  /**
   * 分页的
   */
  private final RowBounds rowBounds;
  /**
   * 对象包装结果处理类
   */
  protected final ObjectWrapperResultHandler<T> objectWrapperResultHandler = new ObjectWrapperResultHandler<>();

  /**
   * 游标的迭代器
   */
  private final CursorIterator cursorIterator = new CursorIterator();
  /**
   * 游标开启判断
   */
  private boolean iteratorRetrieved;

  /**
   * 游标状态,默认是创建未使用
   */
  private CursorStatus status = CursorStatus.CREATED;
  /**
   * 分页索引,默认-1
   */
  private int indexWithRowBound = -1;

  private enum CursorStatus {

    /**
     * A freshly created cursor, database ResultSet consuming has not started.
     * 新创建的游标, ResultSet 还没有使用过
     */
    CREATED,
    /**
     * A cursor currently in use, database ResultSet consuming has started.
     * 游标使用过, ResultSet 被使用
     */
    OPEN,
    /**
     * A closed cursor, not fully consumed.
     * 游标关闭, 可能没有被消费完全
     */
    CLOSED,
    /**
     * A fully consumed cursor, a consumed cursor is always closed.
     * 游标彻底消费完毕, 关闭了
     */
    CONSUMED
  }

  public DefaultCursor(DefaultResultSetHandler resultSetHandler, ResultMap resultMap, ResultSetWrapper rsw, RowBounds rowBounds) {
    this.resultSetHandler = resultSetHandler;
    this.resultMap = resultMap;
    this.rsw = rsw;
    this.rowBounds = rowBounds;
  }

  @Override
  public boolean isOpen() {
    return status == CursorStatus.OPEN;
  }

  @Override
  public boolean isConsumed() {
    return status == CursorStatus.CONSUMED;
  }

  @Override
  public int getCurrentIndex() {
    return rowBounds.getOffset() + cursorIterator.iteratorIndex;
  }

  /**
   * 迭代器获取
   */
  @Override
  public Iterator<T> iterator() {
    // 是否获取过
    if (iteratorRetrieved) {
      throw new IllegalStateException("Cannot open more than one iterator on a Cursor");
    }
    // 是否关闭
    if (isClosed()) {
      throw new IllegalStateException("A Cursor is already closed.");
    }
    iteratorRetrieved = true;
    return cursorIterator;
  }

  @Override
  public void close() {
    // 判断是否关闭
    if (isClosed()) {
      return;
    }

    ResultSet rs = rsw.getResultSet();
    try {
      if (rs != null) {
        rs.close();
      }
    } catch (SQLException e) {
      // ignore
    } finally {
      // 设置游标状态
      status = CursorStatus.CLOSED;
    }
  }

  /**
   * 去到真正的数据行
   */
  protected T fetchNextUsingRowBound() {
    T result = fetchNextObjectFromDatabase();
    while (objectWrapperResultHandler.fetched && indexWithRowBound < rowBounds.getOffset()) {
      result = fetchNextObjectFromDatabase();
    }
    return result;
  }


  /**
   * 从数据库获取数据
   */
  protected T fetchNextObjectFromDatabase() {
    if (isClosed()) {
      return null;
    }

    try {
      objectWrapperResultHandler.fetched = false;
      // 游标状态设置
      status = CursorStatus.OPEN;
      if (!rsw.getResultSet().isClosed()) {
        // 处理数据结果放入，objectWrapperResultHandler
        resultSetHandler.handleRowValues(rsw, resultMap, objectWrapperResultHandler, RowBounds.DEFAULT, null);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    // 获取处理结果
    T next = objectWrapperResultHandler.result;
    // 结果不为空
    if (objectWrapperResultHandler.fetched) {
      // 索引+1
      indexWithRowBound++;
    }
    // No more object or limit reached
    // 如果没有数据, 或者 当前读取条数= 偏移量+限额量
    if (!objectWrapperResultHandler.fetched || getReadItemsCount() == rowBounds.getOffset() + rowBounds.getLimit()) {
      // 关闭游标
      close();
      status = CursorStatus.CONSUMED;
    }
    // 设置结果为null
    objectWrapperResultHandler.result = null;

    return next;
  }

  private boolean isClosed() {
    return status == CursorStatus.CLOSED || status == CursorStatus.CONSUMED;
  }

  private int getReadItemsCount() {
    return indexWithRowBound + 1;
  }

  protected static class ObjectWrapperResultHandler<T> implements ResultHandler<T> {
    /**
     * 数据结果
     */
    protected T result;
    /**
     * 是否null
     */
    protected boolean fetched;

    /**
     * 从{@link ResultContext} 获取结果对象
     *
     * @param context
     */
    @Override
    public void handleResult(ResultContext<? extends T> context) {
      this.result = context.getResultObject();
      context.stop();
      fetched = true;
    }
  }

  protected class CursorIterator implements Iterator<T> {

    /**
     * Holder for the next object to be returned.
     */
    T object;

    /**
     * Index of objects returned using next(), and as such, visible to users.
     */
    int iteratorIndex = -1;

    @Override
    public boolean hasNext() {
      if (!objectWrapperResultHandler.fetched) {
        object = fetchNextUsingRowBound();
      }
      return objectWrapperResultHandler.fetched;
    }

    @Override
    public T next() {
      // Fill next with object fetched from hasNext()
      T next = object;

      if (!objectWrapperResultHandler.fetched) {
        next = fetchNextUsingRowBound();
      }

      if (objectWrapperResultHandler.fetched) {
        objectWrapperResultHandler.fetched = false;
        object = null;
        iteratorIndex++;
        return next;
      }
      throw new NoSuchElementException();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("Cannot remove element from Cursor");
    }
  }
}
