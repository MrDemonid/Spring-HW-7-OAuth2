# Базовый вариант OAuth2.

Данные о пользователях и клиентах храним в БД. В тестовых целях используется H2, но
её почти без переделки можно заменить на что угодно, достаточно немного подправить
файлы schema.sql и data.sql.

Чтобы хранить данные о пользователях в любом удобном нам формате,
был реализован свой AuthenticationProvider, использующий свою реализацию
UserDetailsService и UserDetails.

Для удобства пользования настройками из application.yml, некоторые настройки
оформлены в класс AuthorizationProperties.

Сервер настраивает двух клиентов - один для Web-клиента, другой для программы,
которая работает без пользователя, т.е. авторизируется через Grant CLIENT_CREDENTIALS.

Для запуска Веб-клиента нужно настроить redirectUris адреса в application.yml
(spring.security.oauth2.authorization-server.client-urls), или воспользоваться
универсальным (уже прописан) - 127.0.0.1:8080. Соответственно и запускать клиента
с этих адресов. Запуск с адреса localhost:8080 приводит к ошибке, из-за
конфликта перекрестных ссылок сервера и клиента.

В папке /Tools лежит scratch-файл HTTP-запроса, для получения токена от сервера, 
через CLIENT_CREDENTIALS.

Добавил простую десктопную Swing программу, которая получает данные с сервера ресурсов и 
отображает их. Тот самый случай авторизации через CLIENT_CREDENTIALS.