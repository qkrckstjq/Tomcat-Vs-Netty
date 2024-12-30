# Tomcat-Vs-Netty
__톰캣 서버와 네티 서버의 성능 분석__

# 프로젝트 목적
__정해진 스레드풀에서 요청마다 스레드를 재사용하는 동기적 처리__ 방식인 톰캣과

__정해진 소수의 스레드를 순환(이벤트 루프)시켜 정체되는 스레드 없이 비동기적 처리__ 방식인 네티

이 두 가지를 비교하여 상황에 따라 어떤 결과값이 나오는지 분석해보기

# 테스트 전 기본 세팅
- 서버는 Docker로 띄어져 있음
- 테스트툴 Jmeter와 Docker는 같은 호스트 OS에서 구동 중
- Docker서버는 CPU 코어 사용의 제한을 걸고 구동

---

# Ping/Pong API
이 API는 간단하게 pong을 리턴하는 API로 다음과 같다.

__Tomcat__
```java
@GetMapping("/ping")
public String ping() {
    return "pong";
}
```

__Netty__
```java
@GetMapping("/ping")
public Mono<String> ping() {
    return Mono.just("pong");
}
```
# 결과

## GET /ping

### 1차

| 설정 | 값 |
| - | - |
| 스레드 그룹 | 1000 |
| 램프업 | 0 |
| 반복 | 10 |

__Tomcat__
![pingpong3차](https://github.com/user-attachments/assets/0b9021bf-4ba0-4003-9107-bf444034413e)
![pingpong2차](https://github.com/user-attachments/assets/b3c8266b-9715-4844-8435-4594a7c4de25)
![pingpong1차](https://github.com/user-attachments/assets/e1ef2768-1d25-4390-90f1-44e3b340890d)

TPS : 3064.8

__Netty__
![pingpong3차](https://github.com/user-attachments/assets/ba9479f6-3556-455b-aaae-851aeb686f75)
![pingpong2차](https://github.com/user-attachments/assets/dd618a52-c3ff-4f9f-bf4e-da7de9f73e37)
![pingpong1차](https://github.com/user-attachments/assets/9f62d42f-880b-4a76-88aa-9877c52d02b1)

TPS : 3947.6

__1차 분석__
더 많이 테스트를 했었지만 편차가 매우 컸음 CPU 사용량의 제한을 두고 테스트를 했었지만 테스트별 CPU 사용률의 진폭이 컸음

---

### 2차

| 설정 | 값 |
| - | - |
| 스레드 그룹 | 1000 |
| 램프업 | 0 |
| 반복 | 100 |

__Tomcat__
![1차](https://github.com/user-attachments/assets/216ac626-2a37-487b-9322-54a128428a13)
![3차](https://github.com/user-attachments/assets/ba4f82ee-0b17-4823-8bc6-8d21a12cfc84)
![2차](https://github.com/user-attachments/assets/ca7ecda3-49dd-4571-8995-5adb8b27f66e)

TPS : 2941.8

__Netty__
![1차](https://github.com/user-attachments/assets/c3a278e4-3057-4f72-8dbd-d168dc33937a)
![3차](https://github.com/user-attachments/assets/170a54da-5bb5-4b50-9211-f1c6b71c452b)
![2차](https://github.com/user-attachments/assets/0a62e125-2d7b-4fdd-8f33-6bfb7949a006)

TPS : 3897.6

__2차 분석__
테스트중 많은 부하를 걸다보니 Jmeter자체의 에러가 생기기도 했지만 서버 자체에서의 에러는 없었음

### 결과
CPU 사용량의 진폭이 크고 테스트 TPS의 차이가 좀 났지만 전체적으로 Netty의 TPS가 더 높게 나온다.

---

## GET /calculator
이 API는 요청 시 상당한 양의 for문 연산을 부여하여 블로킹과 논블로킹 방식의 차이점을 확인 하기 위한 API 이다.

__Tomcat__
```java
@GetMapping("/calculator")
public String calculate() {
    tomCatService.calculate();
    return "Calculate done";
}
```

__Netty__
```java
@GetMapping("/calculator")
public Mono<String> calculate() {
    nettyService.calculate();
    return Mono.just("calculate done");
}
```

__.calculate() 메소드__
```java
public void calculate() {
    long num = 0;
    for(long i = 0; i < 1000000000; i++) {
        num += i;
    }
}

/*---------------*/

public void calculate() {
    Mono.fromRunnable(() -> {
        long num = 0;
        for (long i = 0; i < 1000000000; i++) {
            num += i;
        }
    });
}
```

### 1차

| 설정 | 값 |
| - | - |
| 스레드 그룹 | 100 |
| 램프업 | 10 |
| 반복 | 1 |

__Tomcat__
![calculator3차](https://github.com/user-attachments/assets/36e28ecd-7ede-43c6-b94a-1bc9c72c4ca4)
![calculator2차](https://github.com/user-attachments/assets/0da45fee-06aa-408a-a3b9-ce5e438f9a01)
![calculator1차](https://github.com/user-attachments/assets/641f3df4-2684-4774-8ff6-3a606178a2e2)

TPS : 4.1

__Netty__
![calculator3차](https://github.com/user-attachments/assets/1170ec17-0748-45aa-8056-c5dec4d69e37)
![calculator2차](https://github.com/user-attachments/assets/1cbde066-d540-412f-bfe7-cf79785e3479)
![calculator1차](https://github.com/user-attachments/assets/a760b22e-a74b-4e5c-8827-d959911e7cae)

TPS : 10.1

__1차 분석__
해당 테스트는 최대 10 TPS가 나올 수 있게 세팅되어있는데 Tomcat은 4.1, Netty는 10.1이 나오면서 Tomcat 보다 Netty가 높은 TPS 가져왔다.

---

### 2차

| 설정 | 값 |
| - | - |
| 스레드 그룹 | 100 |
| 램프업 | 0 |
| 반복 | 10 |

__Tomcat__
![3차](https://github.com/user-attachments/assets/c00a224a-74c3-4e33-b82c-9f613121ce84)
![2차](https://github.com/user-attachments/assets/910bc1f0-eddf-4be5-bce5-3be5b7b6d9cc)
![1차](https://github.com/user-attachments/assets/420efc15-d207-47c0-833d-907f5c446023)

TPS : 4.06

__Netty__
![3차](https://github.com/user-attachments/assets/56a5ee1a-a119-4533-8c7c-9d0f468e083f)
![2차](https://github.com/user-attachments/assets/f26766f6-4790-49c5-a4b3-baf51c9de9a2)
![1차](https://github.com/user-attachments/assets/0a718208-5c91-4504-b94f-3ca9ccaff291)

TPS : 661.13

__2차 분석__
큰 차이로 Netty가 압도적으로 TPS가 높은 모습을 보여줬다.

### 결과
calculator API의 경우 상당한 양의 계산을 하여 블로킹과 논블로킹의 차이를 볼 수 있는 API인데

둘다 CPU 사용의 제한이 있지만 Tomcat은 하나의 요청당 스레드가 블로킹되어 상당한 차이의 결과를 확인 할 수 있었는데

응답 시간부터 TPS까지 Netty가 압도적인 모습이다.

---

## POST /lock

이 API는 비관적락을 적용한 동시성 제어 테스트로

각 톰캣과 네티에서 비관적락을 걸고 부하 테스트를 했을때 얼마만큼의 차이가 존재하지는 확인하기 위한 API이다.

__Tomcat__
```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT pl FROM PessimisticLock pl WHERE pl.id = :id")
Optional<PessimisticLock> findByIdWithPessimistic(Long id);
```

__Netty__
```java
@Lock(LockMode.PESSIMISTIC_WRITE)
@Query("SELECT * FROM pessimistic_lock WHERE id = :id FOR UPDATE")
Mono<PessimisticLock> findByIdWithPessimistic(Long id);
```
각각 JPA의 @Query, R2DBC의 @Query로 서로 다른 어노테이션의 Query이다.

![제목 없음](https://github.com/user-attachments/assets/fb846c1b-b766-40bb-8753-815cf9ccebf9)
다음과 같이 간단하게 테이블이 구성되어 있다.

모든 요청은 id = 1에 요청을 보내고 딱 100명만 받을 수 있게 락을 걸었다.

### 3차
`1차 테스트와 2차 테스트 결과의 차이가 그리 크지 않아 3차로 기입`

| 설정 | 값 |
| - | - |
| 스레드 그룹 | 10000 |
| 램프업 | 0 |
| 반복 | 10 |

__Tomcat__
![1차](https://github.com/user-attachments/assets/ba822bc9-fd14-4ffc-89bf-72b095f78cdb)
![3차](https://github.com/user-attachments/assets/3dd14329-7cd3-433e-99a3-2551dfc0ebbe)
![2차](https://github.com/user-attachments/assets/61198558-9aab-4226-8a72-de267cda21ea)

TPS : 424.9

__Netty__
![1차](https://github.com/user-attachments/assets/595bf48b-ba12-448e-b5c7-21753dc38ac1)
![3차](https://github.com/user-attachments/assets/96b04134-badd-44de-ae4d-b48efc022480)
![2차](https://github.com/user-attachments/assets/f8e9562a-e93e-4f3d-967f-0c117d62fb2a)

TPS : 778.93

### 결과

비관적락이 걸려있는 상황이라 전체적으로 TPS가 많이 낮아졌지만

비동기처리로 진행된 Netty가 대략 2배 이하로 더 TPS가 높은 모습이 보였다.

응답속도에서는 Netty가 3 ~ 4배 가량 더 빠른 응답속도를 보였다.

비동기 + 통신에서 callback을 받아 더 많은 요청을 수용할 수 있는 R2DBC가 JDBC보다 더 높은 성능을 보인것 같다.


## GET /get

이 API는 MySQL 테이블로부터 간단한 SELECT 문을 요청하여 응답받는 API이다.

__Tomcat__
```java
public String get() {
    JustText justText = justTextRepository.findById(1L).orElseThrow(() ->
            new IllegalArgumentException("없음")
    );
    return justText.getJustText();
}
```

__Netty__
```java
public Mono<String> get() {
    return justTextRepository.findById(1L)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("없음")))
            .map(JustText::getJustText);
}
```

DB와의 연결은 각각 Tomcat은 JPA, Netty는 R2DBC를 통해 통신하고 있다.

부하 테스트에서 1차(스레드 100, 램프업 0, 반복 수 10)에서는 큰 차이가 없어 다음과 같은 세팅에서 테스트했다.

| 설정 | 값 |
| - | - |
| 스레드 그룹 | 1000 |
| 램프업 | 0 |
| 반복 | 10 |

__Tomcat__
![3차](https://github.com/user-attachments/assets/1e8f60f2-1233-4e7d-9d41-2c2c9d4a5569)
![2차](https://github.com/user-attachments/assets/21c210ec-ee43-43ce-9a73-581078e83d50)
![1차](https://github.com/user-attachments/assets/74373240-ec92-4058-ab78-77bc94ebcff4)

TPS : 1016.86

__Netty__
![1차](https://github.com/user-attachments/assets/bdad22df-c8bb-4cd1-a03b-751c72bbfe26)
![3차](https://github.com/user-attachments/assets/c8d52b4a-d918-4d29-ba3d-fbbad1229abf)
![2차](https://github.com/user-attachments/assets/cc0cb1fc-af80-45e9-ab95-f38f5cf09ac6)

TPS : 1502.96

### 결과
Netty가 1.5배 정도 높은 TPS가 나왔다.

톰캣은 블로킹 I/O특성으로 JPA의 과부하로 스레드가 제한되지만 그에 반해 

네티는 논블로킹 I/O특성과 추가적으로 R2DBC특성으로 더 많은 요청을 처리하는 결과인 것 같다.
