import { localAxios } from "@/util/http-commons";

const local = localAxios();

function getSidoList(success, fail) {
    local.get(`/api/sido`).then(success).catch(fail);
}

function getBoard(sidoId, success, fail){
    local.get(`/api/board/${sidoId}`).then(success).catch(fail);
}

function drawGoldCard(requestData, success, fail){
    local.put(`/api/board/gold-card`, JSON.stringify(requestData)).then(success).catch(fail);
}

function getAttractionInfo(mapId, success, fail){
    local.get(`/api/attraction-info/${mapId}`).then(success).catch(fail);
}

function updateLocation(requestData, success, fail){
    local.put(`/api/board/location`, JSON.stringify(requestData)).then(success).catch(fail);
}

function updateScore(sidoId, requestData, success, fail){
    local.put(`/api/score/${sidoId}`, JSON.stringify(requestData)).then(success).catch(fail);
}

export {
    getSidoList,
    getBoard,
    drawGoldCard,
    getAttractionInfo,
    updateLocation,
    updateScore
};
