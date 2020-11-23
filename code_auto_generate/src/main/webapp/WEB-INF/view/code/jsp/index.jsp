<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>测试页面</title>
    <script type="text/javascript" src="/code/static/jquery.min.js"></script>
</head>
<body style="background: #c9c9c9">
<input type="text" id="packageName" placeholder="包名:com.graduation.project" />
<input type="text" id="entityName" placeholder="实体类名: SysUser"  />
<input type="text" id="tablePrefix" placeholder="表前缀: tb_"  />
<button onclick="addProperty()">增加十个属性</button>
<button onclick="delProperty()">删除空白属性</button>
<div id="content" >
    <div class="property" onclick="inputContent(this)">
        <input class="propertyName" type="text" placeholder="属性名称">
        <select class="propertyType">
            <option value ="String">String</option>
            <option value ="Date">Date</option>
            <option value ="Integer">Integer</option>
            <option value ="Byte">Byte</option>
            <option value ="Short">Short</option>
            <option value="Long">Long</option>
            <option value="Float">Float</option>
            <option value="Double">Double</option>
            <option value="char">char</option>
            <option value="boolean">boolean</option>
        </select>
        <input type="checkbox" class="isSingleCondition" name="isSingleCondition"/> 单独作为查询条件
        <input type="checkbox" class="isSingleConditionList" name="isSingleConditionList"/> 是否返回列表
        <input type="checkbox" class="isCondition" name="isCondition"/> 作为复合查询条件
        <input type="checkbox" class="isReturn" name="isReturn"/> 作为返回结果
    </div>

</div>
<button onclick="commit()">提交</button>

</body>

<script>
    function commit() {
        var params = [];
        var propertyNum = $(".property").length;
        for (var i = 0; i < propertyNum; i++) {
            var param = {
                "propertyName": $(".property:eq("+i+") .propertyName:eq(0)").val(),
                "propertyType":  $(".property:eq("+i+") .propertyType:eq(0)").val(),
                "isSingleCondition":  $(".property:eq("+i+") .isSingleCondition:eq(0)").prop('checked'),
                "isSingleConditionList":  $(".property:eq("+i+") .isSingleConditionList:eq(0)").prop('checked'),
                "isCondition":  $(".property:eq("+i+") .isCondition:eq(0)").prop('checked'),
                "isReturn":  $(".property:eq("+i+") .isReturn:eq(0)").prop('checked')
            }
            params.push(param);
        }

        var allParams = {
            "params": params,
            "tablePrefix": $("#tablePrefix").val(),
            "packageName": $("#packageName").val(),
            "entityName": $("#entityName").val()
        }
        $.ajax({
            url: "/generate",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify(allParams),
            success: function (res) {
                console.log(res)
            }
        })
    }
    function addProperty() {
        var propertyNum = $(".property").length;
        for (var i = propertyNum; i < propertyNum+10; i++) {
            var str = '<div class="property" onclick="inputContent(this)">'+
                '            <input class="propertyName" type="text" placeholder="属性名称">'+
                '            <select class="propertyType">'+
                '                <option value ="String">String</option>'+
                '                <option value ="Date">Date</option>'+
                '                <option value ="Integer">Integer</option>'+
                '                <option value ="Byte">Byte</option>'+
                '                <option value ="Short">Short</option>'+
                '                <option value="Long">Long</option>'+
                '                <option value="Float">Float</option>'+
                '                <option value="Double">Double</option>'+
                '                <option value="char">char</option>'+
                '                <option value="boolean">boolean</option>'+
                '            </select>'+
                '<input type="checkbox" class="isSingleCondition"  name="isSingleCondition"/> 单独作为查询条件'+
                '<input type="checkbox" class="isSingleConditionList" name="isSingleConditionList"/> 是否返回列表'+
                '<input type="checkbox" class="isCondition"  name="isCondition"/> 作为复合查询条件'+
                '<input type="checkbox" class="isReturn"  name="isReturn" /> 作为返回结果'+
                '        </div>';
            $("#content").append(str);
        }
    }

    function delProperty() {
        var propertyNum = $(".property").length;
        for (var i = 0; i < propertyNum; i++) {
            if(($(".property:eq("+i+") .propertyName:eq(0)")).val() == '') {
                $(".property:eq("+i+")").remove()
                i--;
            }
        }
    }
    function inputContent(_this) {
        $(".property").css({
            "background": "#c9c9c9"
        });
        $(_this).css({
            "background": "red"
        });
    }

</script>
</html>