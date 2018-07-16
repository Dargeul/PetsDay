
const fs = require('fs');
const getSqlContentMap = require('./util/get-sql-content-map');
const { query } = require('./util/db');
const { initial } = require('./scripts/initial')
const { selectItems } = require('./scripts/select_items');
const { insertItems } = require('./scripts/insert_items');
const { updateItems } = require('./scripts/update_items');
const { deleteItems } = require('./scripts/delete_items');
const Koa = require('koa')
const app = new Koa()
const koaBody = require('koa-body');
// 打印脚本执行日志
const eventLog = function( err , sqlFile, index ) {
  if( err ) {
    console.log(`[ERROR] sql脚本文件: ${sqlFile} 第${index + 1}条脚本 执行失败 o(╯□╰)o ！`)
  } else {
    console.log(`[SUCCESS] sql脚本文件: ${sqlFile} 第${index + 1}条脚本 执行成功 O(∩_∩)O !`)
  }
}

// 获取所有sql脚本内容
let sqlContentMap = getSqlContentMap()

// 执行建表sql脚本
const createAllTables = async () => {
  for( let key in sqlContentMap ) {
    let sqlShell = sqlContentMap[key]
    let sqlShellList = sqlShell.split(';')

    for ( let [ i, shell ] of sqlShellList.entries() ) {
      if ( shell.trim() ) {
        let result = await query( shell )
        if ( result.serverStatus * 1 === 2 ) {
          eventLog( null,  key, i)
        } else {
          eventLog( true,  key, i)
        }
      }
    }
  }
  await initial();
  console.log('sql脚本执行结束！')
  console.log('请按 ctrl + c 键退出！')

}

async function handleRequest(ctx) {

  if (ctx.request.method === 'GET') {
    ctx.body = await selectItems(ctx.request.url.split('?')[0].slice(1), ctx.request.query);

  } else if (ctx.request.method === 'POST') {
	console.log(ctx.request);
    ctx.body = await insertItems(ctx.request.url.slice(1), ctx.request.body);

  } else if (ctx.request.method === 'PUT') {
    ctx.body = await updateItems(ctx.request.url.slice(1), ctx.request.body);

  } else if (ctx.request.method === 'DELETE') {
    ctx.body = await deleteItems(ctx.request.url.split('?')[0].slice(1), ctx.request.query);
  }
}

//createAllTables();
app.use(koaBody());
app.use(handleRequest)

app.listen(3000, () => {
  console.log('[demo] request get is starting at port 3000')
})
