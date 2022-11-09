# Backend
Define your message in messages_<lang>.properties and then you could use it like this:
`messageSource.getMessage(your_message_code, null, LocaleContextHolder.getLocale())`
# Frontend
To translate frontend's components we use the ngx-translate library.