--
--    Copyright 2009-2022 the original author or authors.
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       https://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

drop table product;

create table product
(
    productid varchar(10) not null,
    category  varchar(10) not null,
    name      varchar(80) null,
    descn     varchar(255) null,
    constraint pk_product primary key (productid),
    constraint fk_product_1 foreign key (category)
        references category (catid)
)

