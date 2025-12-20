package com.example.mybatislearning.service;

import com.example.mybatislearning.entity.Organization;
import com.example.mybatislearning.mapper.OrganizationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * OrganizationService
 * Organization管理のビジネスロジックとトランザクション管理
 */
@Service
public class OrganizationService {

    private final OrganizationMapper organizationMapper;

    /**
     * コンストラクタインジェクション
     *
     * @param organizationMapper OrganizationMapper
     */
    @Autowired
    public OrganizationService(OrganizationMapper organizationMapper) {
        this.organizationMapper = organizationMapper;
    }

    /**
     * 新しいOrganizationを作成
     *
     * @param organization 作成するOrganization
     */
    @Transactional
    public void createOrganization(Organization organization) {
        organizationMapper.insert(organization);
    }

    /**
     * IDでOrganizationを取得
     *
     * @param id Organization ID
     * @return 該当するOrganization、存在しない場合はnull
     */
    @Transactional(readOnly = true)
    public Organization getOrganization(Long id) {
        return organizationMapper.selectById(id);
    }

    /**
     * すべてのOrganizationを取得
     *
     * @return Organizationのリスト
     */
    @Transactional(readOnly = true)
    public List<Organization> getAllOrganizations() {
        return organizationMapper.selectAll();
    }

    /**
     * Organizationを更新
     *
     * @param organization 更新するOrganization
     */
    @Transactional
    public void updateOrganization(Organization organization) {
        organizationMapper.update(organization);
    }

    /**
     * Organizationを削除
     *
     * @param id 削除するOrganization ID
     */
    @Transactional
    public void deleteOrganization(Long id) {
        organizationMapper.deleteById(id);
    }
}
