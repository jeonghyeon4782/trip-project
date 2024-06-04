import { localAxios } from "@/util/http-commons";

const local = localAxios();

function listAttractionInfo(success, fail) {
    local.get(`/api/attraction-info`).then(success).catch(fail);
}

function getTopAttractionInfo(success, fail) {
    local.get(`/api/attraction-info/top`).then(success).catch(fail);
}

function getReviewId(boardlogid, success, fail) {
    local.get(`/api/attraction-info/boardlogid/${boardlogid}`).then(success).catch(fail);
}

export {
    listAttractionInfo,
    getTopAttractionInfo,
    getReviewId,
};