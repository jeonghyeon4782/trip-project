import { localAxios } from "@/util/http-commons";

const local = localAxios();

function listReview(param, success, fail) {

  const baseUrl = 'api/review';
  let query = `?pageno=${param.pageno}&pagesize=${param.pagesize}&keyword=${encodeURIComponent(param.keyword)}&order=${param.order}`;

  param.sidos.forEach(sido => {
    query += `&sidos=${sido}`;
  });

  const url = `${baseUrl}${query}`;

  console.log(url);

  local.get(url).then(success).catch(fail);
}

function detailReview(reviewid, success, fail) {
  local.get(`api/review/${reviewid}`).then(success).catch(fail);
}

function registReview(review, file, success, fail) {
  const formData = new FormData();
  formData.append('review', new Blob([JSON.stringify(review)], { type: 'application/json' }));
  if (file) {
    formData.append('file', file);
  }
  console.log("reviewjs formData", formData);

  local.post(`api/review`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then(success).catch(fail);
}

function getModifyReview(reviewid, success, fail) {
  local.get(`api/review/${reviewid}`).then(success).catch(fail);
}

function modifyReview(review, reviewid, file, success, fail) {
  const formData = new FormData();
  formData.append('review', new Blob([JSON.stringify(review)], { type: 'application/json' }));
  if (file) {
    formData.append('file', file);
  }
  console.log("reviewjs formData", formData);

  local.put(`api/review/${reviewid}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then(success).catch(fail);
}

function deleteReview(reviewid, success, fail) {
  local.delete(`api/review/${reviewid}`).then(success).catch(fail);
}

function createLike(reviewid, success, fail) {
  local.post(`api/like/${reviewid}`).then(success).catch(fail);
}

function deleteLike(reviewid, success, fail) {
  local.delete(`api/like/${reviewid}`).then(success).catch(fail);
}

function updateHits(reviewid, success, fail) {
  local.patch(`api/review/${reviewid}`).then(success).catch(fail);
}

export {
  listReview,
  detailReview,
  registReview,
  getModifyReview,
  modifyReview,
  deleteReview,
  createLike,
  deleteLike,
  updateHits
};
