# Track Compounding Trades

A Java Spring Boot application provides a practical solution for tracking compounding trades. 
Users can input key parameters such as initial capital, 
target profit percentage, fixed trade fees, stop-loss orders,
and varying trading periods.

The app then generates daily balance estimates throughout the selected period. Additionally, 
it displays estimated and real all-time profits, 
offering a comprehensive overview of your investment journey. 
Daily, weekly, monthly and yearly targets are presented alongside actual 
achievements for easy monitoring and analysis.

## How targets are calculated

- Base Capital `Which is the initial capital to start the compounding journey with`
- Compounding Percentage `The user decides based on his technical trading levels a fixed target percentage`
- Estimated fees
- Compounding period `It is calculated by days/week/month/year then converted to days`
- Trading cycle `Beside the compounding period, to have more accurate targets a pre defined cycle would help to calculate loss possibilities`
- Estimated loss possibilities `And this is a very important input, it means how many trades you may lose withing a cycle of trades, e.g: "cycle = 10", "estimated loss possibilities =  2" = losing 2 trades for every 10 trades or losing 2 trades after every 8 profitable trades`
- Estimated loss percentage `aka stop loss`
- Official start date `To calculate the period with fixed dates`


## Read this please hh
If you are reading this, I want you to know that the code I made is shi*
I can make it better, but I felt tired and just wanted to finish it hh, maybe someday I will make it clean as much as I can, so please don't judge my ugly code.

## About the Application

This is a simple web application that exposes a REST API. This application uses Maven as the build tool and the current
LTS version of Java, 17. I hope to add more functionality to this application in the future but
for now this project uses the following dependencies:

- Spring Web
- Spring Data JDBC
- Spring Security
- Spring Data JPA
- MySQL Database
- JWT
- Hibernate
- Lombok
- Hikari
- Jakarta Validation


## Deploy Locally
You need to download :
- Apache-Tomcat 10, required for spring-boot 3.0.0
- Change the packaging from JAR to WAR
- Run :
```bash
./mvnw clean install
```
- Change directory to :/target
- Copy the war file and past it to your-tomcat-directory/webapps
- Change directory to : your-tomcat-directory/bin
- In command line run the following
```bash
catalina.bat run
```


## My Links
[MIT LICENSE](https://github.com/achrafaitibba/track-coumpounding-trades/blob/main/LICENSE.txt)

[LinkedIn](https://www.linkedin.com/in/achrafaitibba)

[My Website](https://www.achrafaitibba.com)

[Twitter](https://www.twitter.com/achrafaitibba)
