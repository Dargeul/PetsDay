const { query } = require('../util/db');

let initial = async function() {
	let init_sql = [];
	init_sql.push(`
	INSERT INTO \`user\` (username, password, user_nickname) VALUES ('name1' ,'1234', '甲同学'), ( 'name2' ,'1234', '乙同学'), ('name3' ,'1234', '丙同学');
	`);
	init_sql.push(`
		INSERT INTO \`pet\` (pet_nickname, pet_owner, pet_type, pet_weight, pet_sex, pet_birth, pet_photo) VALUES ('小英',"1",'英短', "20", 'female', '2010-01-02', 'test.jpg'), ('小橘',"1",'橘猫', "20", 'female', '2010-01-02', 'test.jpg'), ('小波',"1",'波斯', "20", 'female', '2010-01-02', 'test.jpg');
	`)
	// init_sql.push(`
	// INSERT INTO \`user_and_pet\` (user_id, pet_id) VALUES ( "1", "1"), ( "2", "2"), ("3", "3");
	// `);
	// init_sql.push(`
	// 	INSERT INTO \`walk\` (walk_stime, walk_etime, walk_pet) VALUES ('2010-01-02 00:00:00', '2010-01-02 00:00:11', "1"), ('2010-01-02 00:00:00', '2010-01-02 00:00:11', "2"), ('2010-01-02 00:00:00', '2010-01-02 00:00:11', "3");
	// `)

	for (var i = 0; i < 100; i++) {
		init_sql.push(`
			INSERT INTO \`hotspot\` (hs_time, hs_user, hs_content, hs_photo) VALUES ('2010-01-02', "1", 'text1${i}', 'test.jpg'), ('2010-01-02', "2", 'text2${i}','test.jpg'), ('2010-01-02', "3", 'text3${i}','test.jpg');
		`);
	}
	init_sql.push(`
		INSERT INTO \`comment\` (com_time, com_user, com_hs, com_content) VALUES ('2010-01-02', "1", "1", "11111"), ('2010-01-02', "2", "2", "22222"), ('2010-01-02', "3", "3", "33333");
	`);

	init_sql.push(`
	INSERT INTO \`pet_and_user\` (pet_id, user_id) VALUES ( "1", "1"), ( "1", "2"), ("1", "3"), ("2", "1"), ("3", "1");
	`);
	init_sql.push(`
	INSERT INTO \`notification\` (notice_status, notice_user, notice_comment) VALUES ( "1", "1", "1"), ( "1", "1", "2"), ("0", "1", "3"), ("0", "2", "1"), ("1", "3", "1");
	`);
	init_sql.push(`
	INSERT INTO \`like\` (like_user, like_hotspot) VALUES ( "1", "1"), ( "1", "2"), ("1", "3"), ("2", "1"), ("3", "1");
	`);
	init_sql.push(`
	INSERT INTO \`pet_and_hotspot\` (pet_id, hs_id) VALUES ( "1", "1"), ( "1", "2"), ("1", "3"), ("2", "1"), ("3", "1");
	`);
	// init_sql.push(`
	// INSERT INTO \`user_and_hotspot\` (user_id, hs_id) VALUES ( "1", "1"), ( "2", "2"), ("3", "3");
	// `);
	init_sql.push(`
		INSERT INTO \`good\` (good_name, good_price, good_count, good_info) VALUES ('猫粮1', '100', "10", '还行'), ('猫粮2', '200', "10", '还行'), ('猫粮3', '300', "10", '还行');
	`)
	for (var i = 0; i < init_sql.length; i++) {
		try {
			await query( init_sql[i] )
		} catch (e) {
			console.log(e);
		} finally {

		}
	}
}

module.exports = {
	initial
};

//
//
//
// curl -X POST --data "walk_stime=2010-01-02 00:00:11&&walk_etime=2010-01-02 00:00:11&&walk_pet=1" 120.78.169.206:3000/walk
//
//
// curl -X POST --data "walk_stime=2010-01-02 00:00:11&&walk_etime=2010-01-02 00:00:11&&walk_pet=1" 120.78.169.206:3000/walk
//
//
// curl -X POST --data "username=丁同学&&password=123&&user_nickname=dingtongxue" 120.78.169.206:3000/user
//
//
// curl -X POST --data "username=丁同学&&password=123&&user_nickname=dingtongxue" 120.78.169.206:3000/user
//
//
// curl -X POST --data "username=丁同学&&password=123&&user_nickname=dingtongxue" 120.78.169.206:3000/user
