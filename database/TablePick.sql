use reserve;

### account 테이블
create table account (
	id VARCHAR(20) PRIMARY KEY,
  type VARCHAR(10) CHECK(type IN('고객','가게주인','관리자')) NOT NULL,
  name VARCHAR(20) NOT NULL,
  password VARCHAR(20) NOT NULL,
  tel VARCHAR(20) NOT NULL
);
### 레스토랑 테이블
create table restaurant (
	idx int PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(30) NOT NULL,
  type VARCHAR(10) CHECK(type IN('한식','중식','일식','양식')) NOT NULL,
  address VARCHAR(50) NOT NULL,
  account_id VARCHAR(20) NOT NULL,
  tel VARCHAR(20) NOT NULL
);
### 예약 테이블
create table reserve (
  idx BIGINT PRIMARY KEY AUTO_INCREMENT,
  account_id VARCHAR(20) NOT NULL,
  restaurant_idx int,
  reserveCount int NOT NULL,
  reservedate datetime NOT NULL,
  registerdate datetime NOT NULL,
  sale BIGINT
);
### 리뷰 테이블
create table review (
	idx BIGINT PRIMARY KEY AUTO_INCREMENT,
  account_id VARCHAR(20),
  restaurant_idx int,
  star INT CHECK (star IN (1,2,3,4,5,6,7,8,9,10)) NOT NULL,
  comment TEXT,
  registerdate datetime NOT NULL
);
### 매뉴 테이블
create table menu (
	idx int PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(30) NOT NULL,
	restaurant_idx int NOT NULL,
	account_id VARCHAR(20) NOT NULL,
	price int NOT NULL
);


##### 제약조건 추가(참조관계)
### member 테이블 컬럼 참조조건 추가
alter table restaurant add constraint restaurant_fk1 foreign key (account_id) REFERENCES account(id) ON DELETE CASCADE;
# foreign key (account_id) REFERENCES account(id)
# restaurant 테이블 컬럼 account_id가 account 테이블 컬럼인 id를 참조하게 함,
# ON DELETE CASCADE : 만일 member_image 테이블의 컬럼 id를 가지는 레코드가 삭제된다면, id를 참조하는 모든 restaurant 테이블의 레코드가 같이 삭제된다.
### reserve 테이블 컬럼 참조조건 추가
alter table reserve add constraint reserve_fk1 foreign key (account_id) REFERENCES account(id) ON DELETE CASCADE;
alter table reserve add constraint reserve_fk2 foreign key (restaurant_idx) REFERENCES restaurant(idx) ON DELETE CASCADE;
### review 테이블 컬럼 참조조건 추가
alter table review add constraint review_fk1 foreign key (account_id) REFERENCES account(id) ON DELETE CASCADE;
alter table review add constraint review_fk2 foreign key (restaurant_idx) REFERENCES restaurant(idx) ON DELETE CASCADE;
### menu 테이블 컬럼 참조조건 추가
alter table menu add constraint menu_fk1 foreign key (restaurant_idx) REFERENCES restaurant(idx) ON DELETE CASCADE;
alter table menu add constraint menu_fk2 foreign key (account_id) REFERENCES account(id) ON DELETE CASCADE;