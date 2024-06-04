import { localAxios } from "@/util/http-commons";

const local = localAxios();

function listComment(reviewId, param, success, fail) {

  const baseUrl = `api/comment/${reviewId}`;
  let query = `?pageno=${param.pageno}&pagesize=${param.pagesize}`;

  const url = `${baseUrl}${query}`;

  console.log(url);

  local.get(url).then(success).catch(fail);
}

function registComment(comment, success, fail) {
  local.post(`/api/comment`, JSON.stringify(comment)).then(success).catch(fail);
}

function modifyComment(comment, commentid, success, fail) {
  local.put(`/api/comment/${commentid}`, JSON.stringify(comment)).then(success).catch(fail);
}

function deleteComment(commentid, success, fail) {
  local.delete(`api/comment/${commentid}`).then(success).catch(fail);
}

export {
  listComment,
  registComment,
  modifyComment,
  deleteComment
};