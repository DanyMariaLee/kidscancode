package com.iloonga.kids

case class IloongaConfig(apiHost: String,
                         apiPort: Int,
                         dbDriver: String,
                         dbUrl: String,
                         dbUser: String,
                         dbPassword: String,
                         apiKeySwagger: String,
                         emailUserAddress: String,
                         emailUserPassword: String,
                         stmpServerHost: String,
                         stmpServerPort: Int,
                         emailSenderAddress: String,
                         emailServiceAddress: String,
                         emailServiceKey: String,
                         iosDeviceKeystoreFile: String
                        )