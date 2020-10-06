## 프로젝트 개요
2020 Naver Campus Hackday<br>
"네이버 Open Api 를 이용한 쇼핑 상품 멀티뷰 앱" 참가 과제로 개인프로젝트로 진행되었습니다.

## 프로젝트 설명
네이버 Open Api 를 통해 쇼핑 상품을 검색하고,<br>
결과를 화면에 타입별로 다르게 노출하여 좋은 UX 를 제공합니다.

## 사용언어
JAVA

## 주요 기능
* 상품 검색 결과 노출 <br>
   상품 검색 결과는 기본적으로 리스트 방식과 그리드 방식 중 선택할 수 있습니다. <br>
   여러 상품의 노출방식은 페이지네이션 방식에 PagerAdapter를 이용하여 스와이프로 페이지를 넘길 수 있는 방식을 도입했습니다.<br><br>
  
* 최근 인기있는 카테고리 노출
   네이버 아이디 로그인을 통해 현재 사용자의 성별, 나이 정보를 받아옵니다. <br>
   네이버 데이터랩 쇼핑인사이트 분야별 트렌드 조회 API를 사용하여 각 카테고리에서 최근 1년의 월간 검색 빈도수를 가져옵니다.<br>
   검색 빈도의 장기 이동 평균과 단기 이동 평균을 비교하여 골든크로스가 발생하면 최근 인기가 상승하고있다고 판단합니다.
   > TrendAnalysis.java
   ```java
  public boolean isIncreasing(int longTerm, int shortTerm) {
        MovingAverage longTermMovingAverage = new SimpleMovingAverage(longTerm);
        MovingAverage shortTermMovingAverage = new SimpleMovingAverage(shortTerm);
        for(TrendRatioData data : trendRatioData) {
            longTermMovingAverage.addValue(data.getRatio().floatValue());
            shortTermMovingAverage.addValue(data.getRatio().floatValue());
        }
        return isAfterGoldenCrossing(longTermMovingAverage.getAverage(), shortTermMovingAverage.getAverage());
    }

    public boolean isAfterGoldenCrossing(float longTermAverage, float shortTermAverage) {
        return longTermAverage < shortTermAverage;
    }
  ```


## 실행화면
![image](https://user-images.githubusercontent.com/37248023/95196451-0e2a8300-0813-11eb-8deb-0ef81dafa520.png)
![image](https://user-images.githubusercontent.com/37248023/95196462-11257380-0813-11eb-9fe9-2b8a54b70dbb.png)
![image](https://user-images.githubusercontent.com/37248023/95196469-1387cd80-0813-11eb-86a1-bb24adb9f249.png)
