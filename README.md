# 起動するのに必要な手順
##環境
Eclipse Marsのpleiades
##DB設定
MySQLをインストール
springbootsample というDBを作る
application.propertiesの下記を編集しアクセスユーザーを設定する
spring.datasource.username=shunya
spring.datasource.password=kiyokawa
##VM引数の設定
Eclipseを開いて実行→実行の構成→引数→VM引数→-Dspring.profiles.active=local を指定する

##実行時のアクセスURLの設定
### httpでアクセスをしたいとき
application.propertiesの下記を編集
server.ssl.enabled=false
URL:http://localhost:8643
### httpsでアクセスしたいとき
application.propertiesの下記を編集
server.ssl.enabled=true
localhost.p12をダブルクリックし、信頼されたルート証明書に登録する
URL:https://localhost:8643

# 起動
Alt+f5でMavenプロジェクトを更新（プロジェクトを右クリック→Maven→更新）
プロジェクトを右クリック→実行→springboot application