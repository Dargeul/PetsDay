const { query } = require('../util/db');

let selectItems = async function(tableName, data) {
  let sql = '';
  switch (tableName) {
    case 'hotspot':
      if (data.page != null) {
        sql = `SELECT hs_time, hs_user, user_nickname, hs_content, hs_photo, hs_id, ifnull(countLike, 0) AS countLike, ifnull(countComment, 0) AS countComment FROM \`hotspot\` LEFT JOIN (SELECT like_hotspot, COUNT(like_hotspot) AS countLike FROM \`like\` GROUP BY like_hotspot) AS count_table ON count_table.like_hotspot=hotspot.hs_id LEFT JOIN (SELECT com_hs, COUNT(com_hs) AS countComment FROM \`comment\` GROUP BY com_hs) AS count_comment_table ON count_comment_table.com_hs=hotspot.hs_id LEFT JOIN user ON user.user_id=hs_user WHERE hs_id <= (SELECT COUNT(*) FROM \`hotspot\`)- ${data.page}*20 ORDER BY hs_id DESC LIMIT 20`;
      } else if (data.pet_id != null) {
        sql = `SELECT hs_time, hs_user, user_nickname, hs_content, hs_photo, hotspot.hs_id, pet_id, ifnull(countLike, 0) AS countLike, ifnull(countComment, 0) AS countComment FROM \`hotspot\` LEFT JOIN pet_and_hotspot ON hotspot.hs_id=pet_and_hotspot.hs_id  LEFT JOIN (SELECT like_hotspot, COUNT(like_hotspot) AS countLike FROM \`like\` GROUP BY like_hotspot) AS count_table ON count_table.like_hotspot=hotspot.hs_id LEFT JOIN (SELECT com_hs, COUNT(com_hs) AS countComment FROM \`comment\` GROUP BY com_hs) AS count_comment_table ON count_comment_table.com_hs=hotspot.hs_id LEFT JOIN user ON user.user_id=hs_user WHERE pet_id = ${data.pet_id}`;
      } else if (data.hs_id != null) {
        sql = `SELECT hs_time, hs_user, user_nickname, hs_content, hs_photo, hotspot.hs_id, ifnull(countLike, 0) AS countLike , ifnull(countComment, 0) AS countComment FROM \`hotspot\`  LEFT JOIN (SELECT like_hotspot, COUNT(like_hotspot) AS countLike FROM \`like\` GROUP BY like_hotspot) AS count_table ON count_table.like_hotspot=hotspot.hs_id LEFT JOIN (SELECT com_hs, COUNT(com_hs) AS countComment FROM \`comment\` GROUP BY com_hs) AS count_comment_table ON count_comment_table.com_hs=hotspot.hs_id LEFT JOIN user ON user.user_id=hs_user WHERE hs_id = ${data.hs_id}`;
      } else {
        sql = `SELECT hs_time, hs_user, user_nickname, hs_content, hs_photo, hs_id, ifnull(countLike, 0) AS countLike , ifnull(countComment, 0) AS countComment FROM \`hotspot\` LEFT JOIN (SELECT like_hotspot, COUNT(like_hotspot) AS countLike FROM \`like\` GROUP BY like_hotspot) AS count_table ON count_table.like_hotspot=hotspot.hs_id LEFT JOIN (SELECT com_hs, COUNT(com_hs) AS countComment FROM \`comment\` GROUP BY com_hs) AS count_comment_table ON count_comment_table.com_hs=hotspot.hs_id LEFT JOIN user ON user.user_id=hs_user ORDER BY \`hs_id\` DESC LIMIT 20`;
      }
      break;
    case 'comment':
      sql = `SELECT com_time, com_user, user_nickname, com_hs, com_content, com_id FROM \`comment\` LEFT JOIN user ON user.user_id=com_user WHERE com_hs = ${data.hs_id}`;
      break;
    case 'pet':
      if (data.user_id != null) {
        if (data.status == 1) {
          sql = `SELECT pet.pet_id, pet_nickname, pet_owner, user_nickname, pet_type, pet_weight, pet_sex, pet_birth, pet_photo, count FROM \`pet\` LEFT JOIN (SELECT pet_id, COUNT(pet_id) AS count FROM pet_and_user GROUP BY pet_id) AS count_table ON count_table.pet_id=pet.pet_id LEFT JOIN user ON user.user_id=pet_owner WHERE pet_owner = ${data.user_id}`;
        } else {
          sql = `
            SELECT pet.pet_id, pet_nickname, pet_owner, user_nickname pet_type, pet_weight, pet_sex, pet_birth, pet_photo, count FROM \`pet\` LEFT JOIN (SELECT pet_id, COUNT(pet_id) AS count FROM pet_and_user GROUP BY pet_id) AS count_table ON count_table.pet_id=pet.pet_id LEFT JOIN pet_and_user ON  pet_and_user.pet_id=pet.pet_id LEFT JOIN user ON user.user_id=pet_owner WHERE pet_and_user.user_id=${data.user_id}
          `;
        }
      } else if (data.hs_id != null) {
        sql = `
          SELECT DISTINCT pet.pet_id, pet_nickname, pet_owner, user_nickname, pet_type, pet_weight, pet_sex, pet_birth, pet_photo, hs_id, count FROM \`pet\` LEFT JOIN (SELECT pet_id, hs_id FROM \`pet_and_hotspot\`)AS joinTable ON pet.pet_id=joinTable.pet_id LEFT JOIN (SELECT pet_id, COUNT(pet_id) AS count FROM pet_and_user GROUP BY pet_id) AS count_table ON count_table.pet_id=pet.pet_id LEFT JOIN user ON user.user_id=pet_owner WHERE hs_id = ${data.hs_id}
        `
      } else if (data.pet_id != null) {
        sql = `
          SELECT pet.pet_id, pet_nickname, pet_type, pet_weight, pet_sex, pet_birth, pet_photo, user_nickname FROM \`pet\` LEFT JOIN pet_and_user ON pet.pet_id = pet_and_user.pet_id LEFT JOIN user ON user.user_id = pet_and_user.user_id  WHERE pet.pet_id = ${data.pet_id}
        `
      }
      break;
    case 'notification':
      if (data.user_id != null) {
        sql = `
          SELECT DISTINCT notice_status, notice_user,  notice_comment, notice_id, com_time, com_user, user_nickname, com_hs, com_content FROM ${tableName} LEFT JOIN comment ON notice_comment=comment.com_id LEFT JOIN user ON user.user_id = com_user WHERE notice_user = ${data.user_id}
        `
      }
      break;
    case 'like':
      sql = `
        SELECT * FROM \`like\` WHERE like_user = ${data.user_id}
      `
      break;
    default:


  }

  // if (tableName == 'hotspot') {
  //   if (data.page != null) {
  //     sql = `SELECT * FROM \`hotspot\` WHERE hs_id <= (SELECT COUNT(*) FROM \`hotspot\`)- ${data.page}*20 ORDER BY hs_id DESC LIMIT 20`;
  //   } else if (data.pet_id != null) {
  //     sql = `SELECT * FROM \`hotspot\` LEFT JOIN pet_and_hotspot ON hotspot.hs_id=pet_and_hotspot.hs_id WHERE pet_id = ${data.pet_id}`;
  //   } else {
  //     sql = `SELECT * FROM \`hotspot\` ORDER BY \`hs_id\` DESC LIMIT 20`;
  //   }
  // } else if (tableName == 'comment') {
  //   sql = `SELECT * FROM \`comment\` WHERE com_hs = ${data.hs_id}`;
  // } else {
  //   sql = `SELECT * FROM ${tableName}`;
  //
  // }
  try {
    if (sql == '') sql = `SELECT * FROM ${tableName}`;
    console.log(sql);
    let dataList = await query( sql )
    return dataList;
  } catch (e) {
    return e;
  } finally {

  }
}


module.exports = {
	selectItems
};



//SELECT * FROM `hotspot` ORDER BY `hs_id` DESC LIMIT 20
//SELECT * FROM `hotspot` LEFT JOIN pet_and_hotspot ON hotspot.hs_id=pet_and_hotspot.hs_id
