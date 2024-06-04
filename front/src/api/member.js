import { localAxios } from "@/util/http-commons";

const local = localAxios();

function detailMember(success, fail) {
    local.get(`/api/member`).then(success).catch(fail);
}

function getModifyMember(password, success, fail) {
    local.get(`/api/member/${password}`).then(success).catch(fail);
}

function modifyMember(member, file, success, fail) {
    const formData = new FormData();
    formData.append('member', new Blob([JSON.stringify(member)], { type: 'application/json' }));
    if (file) {
        formData.append('file', file);
    }
    console.log("memberjs formData", formData);

    local.put(`api/member`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    }).then(success).catch(fail);
}

function deleteMember(password, success, fail) {
    local.delete(`api/member/${password}`).then(success).catch(fail);
}

export {
    detailMember,
    getModifyMember,
    modifyMember,
    deleteMember
};