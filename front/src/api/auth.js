import { localAxios } from "@/util/http-commons";

const local = localAxios();

function loginMember(member, success, fail) {
    console.log("authjs member", member);
    local.post(`/auth/login`, JSON.stringify(member)).then(success).catch(fail);
}

function registMember(member, success, fail) {
    console.log("authjs member", member);
    local.post(`/auth/register`, JSON.stringify(member)).then(success).catch(fail);
}

function oauthMember(requestData, success, fail) {
    console.log("authjs member", requestData);
    local.post('/auth/login/social', JSON.stringify(requestData)).then(success).catch(fail);
}

function logoutMember(success, fail) {
    console.log("authjs logout");
    local.post('/auth/logout').then(success).catch(fail);
}

function duplicateCheckMemberId(requestData, success, fail){
    local.post('http://localhost:8080/auth/duplicate-check-id', JSON.stringify(requestData)).then(success).catch(fail);
}

function duplicateCheckNickname(requestData, success, fail){
    local.post('http://localhost:8080/auth/duplicate-check-nickname', JSON.stringify(requestData)).then(success).catch(fail);
}

function authenticationEmail(requestData, success, fail){
    local.post('http://localhost:8080/auth/authentication-email', JSON.stringify(requestData)).then(success).catch(fail);
}

function resendAuthenticationKey(requestData, success, fail){
    local.post('http://localhost:8080/auth/mail/resend-authentication-key', JSON.stringify(requestData)).then(success).catch(fail);
}

function deleteAuthenticationKey(requestData, success, fail){
    local.post('http://localhost:8080/auth/mail/delete-authentication-key', JSON.stringify(requestData)).then(success).catch(fail);
}

function checkAuthenticationKey(requestData, success, fail){
    local.post('http://localhost:8080/auth/mail/check-authentication-key', JSON.stringify(requestData)).then(success).catch(fail);
}

function findMemberId(requestData, success, fail){
    local.post('http://localhost:8080/auth/find-memberId', JSON.stringify(requestData)).then(success).catch(fail);
}

function findPassword(requestData, success, fail){
    local.post('http://localhost:8080/auth/find-password', JSON.stringify(requestData)).then(success).catch(fail);
}

export {
    loginMember,
    registMember,
    oauthMember,
    duplicateCheckMemberId,
    duplicateCheckNickname,
    authenticationEmail,
    resendAuthenticationKey,
    deleteAuthenticationKey,
    checkAuthenticationKey,
    logoutMember,
    findPassword,
    findMemberId
};
