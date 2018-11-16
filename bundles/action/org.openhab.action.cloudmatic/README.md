# CloudMatic Actions

This add-on provides services for your rules to send an E-Mail or SMS* via [CloudMatic](https://www.cloudmatic.de/).

The `to` paremeter can contain a semicolon-separated list of email addresses.

** [SMS-credits](https://www.cloudmatic.de/cloudmatic-notifyme.html) required to send SMS

## Actions

- `sendCloudMaticMail(String to, String subject, String message)`: Sends an E-Mail via CloudMatic
- `sendCloudMaticSMS(String phoneNumber, String message)`: Sends a SMS via CloudMatic

## Configuration
This action service can be configured via the `services/cloudmatic.cfg` file.
###Sample Configuration
```
id=00110
username=yourusername
password=yourhttppassword
```
## Examples

```javascript
sendCloudMaticMail("you@email.net", "Mail subject", "Mail message body")
sendCloudMaticSMS("004912348571", "SMS title", "SMS message body")
```

## Rule Example
```
rule "CloudMatic Rules"
when
    System started
then
    sendCloudMaticMail("you@mail.de", "Mail subject", "Mail message body");
    sendCloudMaticSMS("0049122348571", "SMS message");
end
