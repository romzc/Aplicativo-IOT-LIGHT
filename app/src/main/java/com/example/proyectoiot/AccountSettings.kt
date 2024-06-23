package com.example.proyectoiot

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler
import com.amazonaws.regions.Regions

class AccountSettings(private val appContext: Context) {
    private val poolID = "us-east-2_Si3QC9izF"
    private val clientID = "3labg7v4bmmojgt27qppstgabh"
    private val clientSecret = "1kb16snp0q7qpkohgrmgomred3313tbdbsmqc5gujba63psuouhu"
    private val awsRegion = Regions.US_EAST_2
    private lateinit var userPool: CognitoUserPool
    private lateinit var userAttributes: CognitoUserAttributes
    private lateinit var userPassword : String

    fun connectCognito() {
        userAttributes = CognitoUserAttributes()
        userPool = CognitoUserPool(appContext, poolID, clientID, clientSecret, awsRegion)
    }

    fun addAttribute(key: String?, value: String?) {
        userAttributes.addAttribute(key, value)
    }

    fun signUpInBackground(userId: String?, password: String?, signUpCallback: SignUpHandler) {
        userPool.signUpInBackground(userId, password, userAttributes, null, signUpCallback)
    }

    fun userLogin(userId: String?, password: String?, authenticationHandler: AuthenticationHandler) {
        val cognitoUser = userPool.getUser(userId)
        userPassword = password!!
        cognitoUser.getSessionInBackground(authenticationHandler)
    }

    fun getUserDetails(getDetailsHandler: GetDetailsHandler) {
        val cognitoUser = userPool.currentUser
        cognitoUser.getDetailsInBackground(getDetailsHandler)
    }

    fun changePassword(newPassword: String, genericHandler: GenericHandler) {
        val cognitoUser = userPool.currentUser
        cognitoUser.changePasswordInBackground(userPassword, newPassword, genericHandler)
    }

    fun updateDetails(newUsername: String, newEmail: String, updateAttributesHandler : UpdateAttributesHandler){
        val cognitoUser = userPool.currentUser
        val userAttributesNew = CognitoUserAttributes()
        userAttributesNew.addAttribute("preferred_username", newUsername)
        userAttributesNew.addAttribute("email", newEmail)
        cognitoUser.updateAttributesInBackground(userAttributesNew, updateAttributesHandler)
    }

    fun signOut() {
        val cognitoUser = userPool.currentUser
        cognitoUser.signOut()
    }
}