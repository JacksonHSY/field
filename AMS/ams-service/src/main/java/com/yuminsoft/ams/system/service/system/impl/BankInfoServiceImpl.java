package com.yuminsoft.ams.system.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuminsoft.ams.system.dao.system.BankInfoMapper;
import com.yuminsoft.ams.system.domain.system.Banks;
import com.yuminsoft.ams.system.service.system.BankInfoService;
import com.yuminsoft.ams.system.vo.pluginVo.RequestPage;
import com.yuminsoft.ams.system.vo.pluginVo.ResponsePage;

@Service
public class BankInfoServiceImpl implements BankInfoService {
	
	@Autowired
	private BankInfoMapper bankInfoMapper;

	@Override
	public ResponsePage<Banks> getPageList(RequestPage requestPage,Banks banks) {
		ResponsePage<Banks> rp = new ResponsePage<Banks>();
		PageHelper.startPage(requestPage.getPage(), requestPage.getRows());
		List<Banks> list = bankInfoMapper.findAllConditions(banks);
		rp.setRows(list);
		rp.setTotal(((Page<Banks>)list).getTotal());
		return rp;
	}

	@Override
	public void save(Banks banks) {
		bankInfoMapper.save(banks);
	}

	@Override
	public int update(Banks banks) {
		return bankInfoMapper.update(banks);
	}

	@Override
	public Banks findById(long id) {
		return bankInfoMapper.findById(id);
	}

	@Override
	public int delete(String[] ids) {
		return bankInfoMapper.deletes(ids);
	}
}
