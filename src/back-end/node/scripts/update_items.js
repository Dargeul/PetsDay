const { query } = require('../util/db');

let updateItems = async function(tableName, data) {
  let sql = ``;
  switch (tableName) {
    case 'user':
      sql = `
        UPDATE ${tableName} SET password = '${data.password}', user_nickname = '${data.user_nickname}' WHERE user_id = '${data.user_id}' ;
      `;
      break;
    case 'pet':
			sql = `
				UPDATE ${tableName} SET pet_nickname = '${data.pet_nickname}', pet_owner = '${data.pet_owner}', pet_type = '${data.pet_type}', pet_weight = '${data.pet_weight}', pet_sex = '${data.pet_sex}', pet_birth = '${data.pet_birth}', pet_photo = '${data.pet_photo}' WHERE pet_id = '${data.pet_id}' ;
			`
      break;
    case 'notification':
      sql = `
        UPDATE ${tableName} SET notice_status = '${data.notice_status}' WHERE notice_id = '${data.notice_id}' ;
      `;
      break;
    default:


  }

// UPDATE Person SET FirstName = 'Fred' WHERE LastName = 'Wilson'

  console.log(sql);
  try {

    let dataList = await query( sql )
    return dataList;
  } catch (e) {
    return e;
  } finally {

  }
}

module.exports = {
	updateItems
};

//

// curl -X PUT --data "password=345&&user_nickname=甲同学1&&user_id=1" 127.0.0.1:3000/user
//
// curl -X PUT --data "pet_nickname=小黑&&pet_owner=1&&pet_type=暹罗&&pet_weight=30&&pet_sex=male&&pet_birth=2010-01-02&&pet_id=1" 127.0.0.1:3000/pet
