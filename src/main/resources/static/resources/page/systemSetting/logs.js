layui.use(['table'],function(){
	var table = layui.table;

	//系统日志
    table.render({
        elem: '#logs',
        url : '../../json/logs.json',
        cellMinWidth : 95,
        page : true,
        height : "full-20",
        limit : 20,
        limits : [10,15,20,25],
        id : "systemLog",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'logId', title: '序号', width:60, align:"center"},
            {field: 'url', title: '请求地址', width:350},
            {field: 'method', title: '操作方式', align:'center',templet:function(d){
                if(d.method.toUpperCase() == "GET"){
                    return '<span class="layui-blue">'+d.method+'</span>'
                }else{
                    return '<span class="layui-red">'+d.method+'</span>'
                }
            }},
            {field: 'ip', title: '操作IP',  align:'center',minWidth:130},
            {field: 'timeConsuming', title: '耗时', align:'center',templet:function(d){
                return '<span class="layui-btn layui-btn-normal layui-btn-xs">'+d.timeConsuming+'</span>'
            }},
            {field: 'isAbnormal', title: '是否异常', align:'center',templet:function(d){
                if(d.isAbnormal == "正常"){
                    return '<span class="layui-btn layui-btn-green layui-btn-xs">'+d.isAbnormal+'</span>'
                }else{
                    return '<span class="layui-btn layui-btn-danger layui-btn-xs">'+d.isAbnormal+'</span>'
                }
            }},
            {field: 'operator',title: '操作人', minWidth:100, templet:'#newsListBar',align:"center"},
            {field: 'operatingTime', title: '操作时间', align:'center', width:170}
        ]]
    });
 	
})
