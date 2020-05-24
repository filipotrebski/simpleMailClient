


Utworzenie serwera testowego
```shell script
docker run --rm -p 1025:1025 -p 8025:8025 mailhog/mailhog
```

Budowanie aplikacji:
```shell script
gradle distZip
```

Konfiguracja aplikacji:
W pliku `$HOME/.simplemail/settings.properties` należy wpisać adres serwera SMTP i IMAP oraz dane do autoryzacji:
```properties
smtp.host=smtp.email.com
smtp.port=587
imap.host=imap.email.com
imap.port=993
user=user_name
password=sercret_password
```


Przykładowe użycie:
```shell script
./simpleMailClient send -s "email subject" -to john@email.com -cc "cc@email.com"  -bcc "bcc@email.com" -a file.txt -a photo.jpg 
```