use database reserve;


/* 조인문 기본 구조
 * [테이블1] [JOIN 종류] [테이블2] on [조인 조건]
 *  */

SELECT r.*
FROM review r
JOIN reserve re ON r.reserve_idx = re.idx
WHERE re.account_id = 'cust01';

SELECT r.*
-- review 테이블의 모든 컬럼을 선택
FROM review r JOIN reserve re
-- review r 테이블과 reserve re 테이블를 조인, review 테이블과 reserve 테이블 alias 설정
ON r.reserve_idx = re.idx
-- 테이블 두개 연결 조건(공통된 값으로 연결)
WHERE re.account_id = 'cust02';

-------------------------------------------

UPDATE review r
JOIN reserve re ON r.reserve_idx = re.idx
SET r.star = 2,
    r.comment = '리테 맛좋다!'
WHERE re.account_id = 'cust05'
  AND r.idx = 12;
  
/* 
 * account 테이블의 id 컬럼값과 review 테이블의 idx 값에 해당하는
 * review 테이블의 star, comment 컬럼값 변경 하는 sql 문
 * */
  
UPDATE review r
JOIN reserve rs ON r.reserve_idx = rs.idx
SET r.star = 3, r.comment = '가격에 비해서 너무 음식이 질이 낮습니다.'
WHERE rs.account_id = 'cust05' AND r.idx = 5;
	
UPDATE review r
-- 어떤 테이블의 데이터를 변경할 것인지 지정 : 예시 sql문에서는 review 테이블의 로우를 변경한다.
JOIN reserve rs ON r.reserve_idx = rs.idx
-- 테이블의 데이터를 연결 작업 : 예시 sql문에서는 reserve 테이블과 review 테이블을
-- reserve_idx 컬럼과 idx 컬럼을 기준으로 내부 조인 한다.
SET r.star = 3, r.comment = '가격에 비해서 너무 음식이 질이 낮습니다.'
-- 어떤 컬럼의 값을 어떻게 바꿀것인지 작성 : 예시 sql문에서는 review r 테이블의 star 컬럼값을 3,
-- review r 테이블의 comment 컬럼값을 '가격에 비해서 너무 음식이 질이 낮습니다.'로 바꾼다.
WHERE rs.account_id = 'cust05' AND r.idx = 5;
-- 어떤 로우의 값을 바꿀 것인지 지정 : 예시 sql문에서는 rs.account_id 컬럼값이 'cust05' 이면서
-- r.idx 컬럼값이 5 로우의 값이 변경 된다.