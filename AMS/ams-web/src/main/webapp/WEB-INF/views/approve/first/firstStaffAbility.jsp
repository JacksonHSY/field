<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
	.switch {
		border: 1px solid #d1d1d1;
		border-radius: 5px;
		height: 25px;
		padding-left: 10px;
		margin-left: 5px;
		width: 50%;
		min-width: 70px;
		max-width: 100px;
		font-size: 14px;
	}

	.switch:hover {
		border-color: #66afe9;
		box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px rgba(102, 175, 233, 0.6);
	}

	.switch:focus {
		border-color: #66afe9;
		box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px rgba(102, 175, 233, 0.6);
	}

	label.error {
		color: red;
	}

	select.error{
		border: 1px solid #ffa8a8;
		border-radius: 5px;
		height: 25px;
		padding-left: 10px;
		margin-left: 5px;
		width: 50%;
		min-width: 70px;
		max-width: 100px;
		font-size: 14px;
		background-color: #fff3f3;
	}

	select.error:hover{
		border-color: #ffa8a8;
		box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px rgba(102, 175, 233, 0.6);
	}


	.mymultiple {
		float: right;
		width: 80px;
		height: 20px;
		position: relative;

	}
	.mymultiple input{
		border: 1px solid #d1d1d1;
		width: 68px;
		height: 23px;
		margin: 0px;
		border-radius: 5px;
		text-align: center;
		position: relative;
		z-index: 0;


	}
	.mymultiple dl {
		cursor:pointer;
		z-index: 1;
		display: none;
		width: 70px;
		position: relative;
		background-color: white;
		border: 1px solid #d1d1d1;
		margin-left: 10px;
		height: 150px;
		overflow: scroll;
	}
	.mymultiple dl.show {
		display: block;
	}

	.mymultiple dl dd{
		height: 23px;
		line-height: 10px;
		color: black;
		text-align: center;
	}
	.mymultiple dl dd.ddselect{
		background-color: #5599FE;
	}

</style>
<form id="abilityConfigForm" class="ms-controller" ms-controller="page">
	<table class="table_list W100" style="text-align: right;">
		<thead>
        <c:forEach var="i" begin="1" end="6">
            <td style="padding: 5px; white-space: nowrap">优先级&nbsp;&nbsp;&nbsp;综合信用评级</td>
        </c:forEach>
			<tr>
				<td style="padding: 5px; white-space: nowrap">
					产品列表:
					<select style="width: 10px" class="switch" ms-on-change="changeAll($event)">
						<option></option>
						<option value="0">禁止</option>
						<option value="1">允许</option>
						<option value="2">优先</option>
					</select>
					<input hidden value="all">
					<div class="mymultiple">
						<input readonly onmouseover="this.title= typeof $(this).data('val')=='undefined'?'':$(this).data('val')">
							<dl>
                                <dd data-val="优秀">优秀</dd><dd data-val="良好">良好</dd><dd data-val="四级关注">四级关注</dd><dd data-val="三级关注">三级关注</dd>
                                <dd data-val="二级关注">二级关注</dd><dd data-val="一级关注">一级关注</dd><dd data-val="婉拒">婉拒</dd>
							</dl>
					</div>
				</td>
				<td ms-for="($index, areaItem) in areaList" style="padding: 5px;white-space: nowrap">
					{{ areaItem.name }}:
					<select style="width: 10px" ms-on-change="changeByArea(areaItem, $event)" class="switch ability">
						<option></option>
						<option value="0">禁止</option>
						<option value="1">允许</option>
						<option value="2">优先</option>
					</select>
					<input  type="hidden" name="areaCode" ms-duplex="areaItem.orgCode" value="areaItem.orgCode">
					<div class="mymultiple">
						<input readonly class="area" onmouseover="this.title= typeof $(this).data('val')=='undefined'?'':$(this).data('val')">
						<dl>
                            <dd data-val="优秀">优秀</dd><dd data-val="良好">良好</dd><dd data-val="四级关注">四级关注</dd><dd data-val="三级关注">三级关注</dd>
                            <dd data-val="二级关注">二级关注</dd><dd data-val="一级关注">一级关注</dd><dd data-val="婉拒">婉拒</dd>
						</dl>
					</div>
				</td>
			</tr>
		</thead>
		<tbody>
			<tr ms-for="($index, item) in abilityVoList">
				<td style="padding: 5px; white-space: nowrap">
					{{ item.productName + '-' + item.type }}:
					<select style="width: 10px" class="switch ability" ms-on-change="changeByProduct(item, $event)">
						<option></option>
						<option value="0">禁止</option>
						<option value="1">允许</option>
						<option value="2">优先</option>
					</select>
					<input type="hidden" name="product" ms-duplex="item.productCode + '-' + item.type" value="item.productCode + '-' + item.type">
					<div class="mymultiple">
						<input readonly class="product" onmouseover="this.title= typeof $(this).data('val')=='undefined'?'':$(this).data('val')">
						<dl>
                            <dd data-val="优秀">优秀</dd><dd data-val="良好">良好</dd><dd data-val="四级关注">四级关注</dd><dd data-val="三级关注">三级关注</dd>
                            <dd data-val="二级关注">二级关注</dd><dd data-val="一级关注">一级关注</dd><dd data-val="婉拒">婉拒</dd>
						</dl>
					</div>
				</td>
				<td ms-for="($index, ability) in item.staffAbilityList" style="padding: 5px;white-space: nowrap">
					<select style="width: 10px" ms-attr="{id: ability.productCode + '-' + ability.type + '_' + ability.areaCode, name: ability.productCode + '-' + ability.type + '_' + ability.areaCode}"
							ms-duplex="ability.value" class="switch ability" data-msg="请选择" required>
						<option></option>
						<option value="0">禁止</option>
						<option value="1">允许</option>
						<option value="2">优先</option>
					</select>
					<div class="mymultiple">
						<input required data-msg="" onmouseover="this.title= typeof $(this).data('val')=='undefined'?'':$(this).data('val')" readonly ms-attr="{id: ability.productCode + '-' + ability.type + '_' + ability.areaCode +'-'+'text'}" class="text" ms-class="['area-'+ability.areaCode,ability.productCode + '-' + ability.type]">
						<dl>
                            <dd data-val="优秀">优秀</dd><dd data-val="良好">良好</dd><dd data-val="四级关注">四级关注</dd><dd data-val="三级关注">三级关注</dd>
                            <dd data-val="二级关注">二级关注</dd><dd data-val="一级关注">一级关注</dd><dd data-val="婉拒">婉拒</dd>

						</dl>
					</div>
					<input hidden ms-attr="{id: ability.productCode + '-' + ability.type + '_' + ability.areaCode +'-'+'level', name: ability.productCode + '-' + ability.type + '_' + ability.areaCode}"
						   ms-duplex ="ability.comCreditRating" class="level">
				</td>
			</tr>
		</tbody>
	</table>
</form>
<script type="text/javascript" src="${ctx}/resources/lib/validation/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/system/personReceiveConfig/firstStaffAbility.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/system/personReceiveConfig/firstStaffAbilityCreditRating.js"></script>
<script type="text/javascript">
    pageInit({
        $id: 'page',
        type: '${type}',
        userList: ${usersJson == null  ? "[]" : usersJson},
        areaList: ${areaListJson == null ? "[]" : areaListJson},
        productList: ${productListJson == null ? "[]" : productListJson},
        abilityList: ${abilityListJson == null ? "[]" : abilityListJson},
        abilityVoList: ${abilityVoListJson == null ? "[]" : abilityVoListJson},
    });

</script>