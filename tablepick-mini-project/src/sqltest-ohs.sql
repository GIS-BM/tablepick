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