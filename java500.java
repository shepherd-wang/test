package com.ucloud.paas.proxy.aaaa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
modify version 3
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ucloud.paas.agent.PaasException;
import com.ucloud.paas.proxy.ProxyTestBase;
import com.ucloud.paas.proxy.aaaa.entity.AccountEntity;
import com.ucloud.paas.proxy.aaaa.entity.AppEntity;
import com.ucloud.paas.proxy.aaaa.entity.FavoriteGroup;
import com.ucloud.paas.proxy.aaaa.entity.FavoriteGroupAndMember;
import com.ucloud.paas.proxy.aaaa.entity.FavoriteGroupMember;
import com.ucloud.paas.proxy.aaaa.entity.GroupEntity;
import com.ucloud.paas.proxy.aaaa.entity.OrgEntity;
import com.ucloud.paas.proxy.aaaa.entity.OrgShortNameStructure;
import com.ucloud.paas.proxy.aaaa.entity.OrgStructure;
import com.ucloud.paas.proxy.aaaa.entity.ResourceEntity;
import com.ucloud.paas.proxy.aaaa.entity.RoleDimensionsData;
import com.ucloud.paas.proxy.aaaa.entity.RoleEntity;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
import com.ucloud.paas.proxy.aaaa.util.JSONUtils;
import com.ucloud.paas.proxy.aaaa.util.PaasAAAAException;
import com.ucloud.paas.proxy.common.Pagination;
import com.ucloud.paas.proxy.common.SortDirectionEnum;
import testmodify


public class AAAAServiceTest01 extends ProxyTestBase {

    private AAAAService aaaa = new AAAAService();

    private static final int WRONG_ID = 999999;

    @Test(enabled = true)
    public void findOrgByOrgID() throws Exception {
        OrgEntity entity = aaaa.findOrgByOrgID(1);
        Assert.assertNotNull(entity);
    }

    @Test(enabled = true)
    public void findOrgListByParentID() throws Exception {
        List<OrgEntity> list = aaaa.findOrgListByParentID(1);
        Assert.assertTrue(list != null && list.size() > 0);
        for (OrgEntity o : list) {
            System.out.println(JSONUtils.toString(o));
        }
    }

    @Test(enabled = true)
    public void findOrgEntityByParams() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        String orgCode = "abc";
        params.put("hrOrgCode", orgCode);
        // params.put("orgName", orgName);
        int pageNum = 1;
        int pageSize = 10;
        String sortColumn = "orgName";
        SortDirectionEnum sortDirection = SortDirectionEnum.ASC;
        Pagination page = aaaa.findOrgEntityByParams(params, pageNum, pageSize, sortColumn, sortDirection);
        Assert.assertNotNull(page);
        System.out.println(JSONUtils.toString(page));
        List<?> data = page.getResult();
        Assert.assertNotNull(data);
        for (Object obj : data) {
            Assert.assertNotNull(obj);
            OrgEntity org = (OrgEntity) obj;
            System.out.println(JSONUtils.toString(org));
            Assert.assertEquals(org.getHrOrgCode(), orgCode);
        }

    }

    @Test(enabled = true)
    public void findOrgByAreaId() throws Exception {
        OrgEntity orgEntity = aaaa.findOrgByAreaId(1);
        Assert.assertTrue(orgEntity != null);
        System.out.println(JSONUtils.toString(orgEntity));
        //
        orgEntity = aaaa.findOrgByAreaId(2);
    }

    @Test(enabled = true)
    public void findUserEntityByParams() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        String hrEmpCode = "0630525";
        params.put("hrEmpCode", hrEmpCode);
        // params.put("orgName", orgName);
        int pageNum = 1;
        int pageSize = 10;
        String sortColumn = "hrEmpCode";
        SortDirectionEnum sortDirection = SortDirectionEnum.ASC;
        Pagination page = aaaa.findUserEntityByParams(params, pageNum, pageSize, sortColumn, sortDirection);
        Assert.assertNotNull(page);
        System.out.println(JSONUtils.toString(page));
        List<?> data = page.getResult();
        Assert.assertNotNull(data);
        Assert.assertTrue(data.size() > 0);
        for (Object obj : data) {
            Assert.assertNotNull(obj);
            UserEntity user = (UserEntity) obj;
            System.out.println(JSONUtils.toString(user));
            Assert.assertEquals(user.getHrEmpCode(), hrEmpCode);
        }

    }

    @Test(enabled = true)
    public void findUserEntityByRoleAndOrg() throws PaasException, PaasAAAAException {
        Integer roleId = 4270030;
        Integer cloudOrgId = 11000;
        int pageNum = 1;
        int pageSize = 10;
        String portalAccountId = null; // "caokj";// null;
        String empName = "";
        Pagination page = aaaa.findUserEntityByRoleAndOrg(roleId, cloudOrgId, pageNum, pageSize, portalAccountId, empName);
        Assert.assertNotNull(page);
        Assert.assertTrue(page.getResult().size() > 0);
        for (Object obj : page.getResult()) {
            System.out.println(JSONUtils.toString(obj));
        }
    }

    @Test(enabled = true)
    public void getAsboluteOrgHierarchy() throws PaasAAAAException {
        int cloudOrgId = 11301;
        List<OrgEntity> lst = aaaa.getAsboluteOrgHierarchy(cloudOrgId);
        Assert.assertTrue(lst.size() > 0);
        if (lst.size() > 0) {
            OrgEntity org = lst.get(0);
            System.out.println("Level 0 :" + org.getCloudOrgId() + "," + org.getParentCloudOrgId() + "," + org.getOrgName() + "," + org.getOrgCode() + "," + org.getParentOrgCode());
            Integer parentId = lst.get(0).getCloudOrgId();
            for (int i = 1; i < lst.size(); i++) {
                org = lst.get(i);
                System.out.println("Level " + i + " :" + org.getCloudOrgId() + "," + org.getParentCloudOrgId() + "," + org.getOrgName() + "," + org.getOrgCode() + "," + org.getParentOrgCode());
                Assert.assertEquals(parentId, org.getParentCloudOrgId());
                parentId = org.getCloudOrgId();
            }
        }

    }

    @Test(enabled = true)
    public void findCompanysByOrgId() throws PaasAAAAException {
        int cloudOrgId = 123140246; // 11301 石家庄市分公司
        OrgStructure orgs = aaaa.findCompanysByOrgId(cloudOrgId);
        Assert.assertNotNull(orgs);
        System.out.println(JSONUtils.toString(orgs));
        Assert.assertEquals(orgs.getGroupCompany().getOrgName(), "中国联通");
    }

    @Test(enabled = true)
    public void findCompanysExtByOrgId() throws PaasAAAAException {
        int cloudOrgId = 123140246; // 11301 石家庄市分公司
        OrgStructure orgs = aaaa.findCompanysExtByOrgId(cloudOrgId);
        Assert.assertNotNull(orgs);
        System.out.println(JSONUtils.toString(orgs));
        Assert.assertEquals(orgs.getGroupCompany().getOrgName(), "中国联通");
    }
    
    @Test(enabled = false)
    public void findCompanysBrevityByOrgId() throws PaasAAAAException {
        int cloudOrgId = 1;
        OrgShortNameStructure orgs = aaaa.findCompanysBrevityByOrgId(cloudOrgId);
        Assert.assertNotNull(orgs);
        System.out.println(JSONUtils.toString(orgs));
    }

    @Test(enabled = true)
    public void findCompanysByParentOrgId() throws PaasAAAAException {
        int cloudOrgId = 1; // 1, 5991
        List<OrgEntity> orgs = aaaa.findCompanysByParentOrgId(cloudOrgId);
        Assert.assertTrue(orgs != null && orgs.size() > 0);
        for (OrgEntity org : orgs) {
            System.out.println(JSONUtils.toString(orgs));
            Assert.assertTrue("3,5,7".indexOf(org.getOrgCategory()) > -1);
        }
    }

    @Test(enabled = true)
    public void findUserListByOrgID() throws Exception {
        List<UserEntity> list = aaaa.findUserListByOrgID(1);
        Assert.assertTrue(list != null && list.size() > 0);
        for (UserEntity u : list) {
            System.out.println(JSONUtils.toString(u));
        }
    }

    @Test(enabled = true)
    public void findUserbyUserID() throws Exception {
        UserEntity entity = aaaa.findUserbyUserID(74);
        Assert.assertNotNull(entity);
        System.out.println(JSONUtils.toString(entity));
        Assert.assertNotNull(entity.getAccount().getAccountType());
        entity = aaaa.findUserbyUserID(WRONG_ID);
        Assert.assertNull(entity);
    }

    @Test(enabled = true)
    public void findUserByCloudAccountId() throws Exception {
        UserEntity entity = aaaa.findUserByCloudAccountId(12);
        Assert.assertNotNull(entity);
        Assert.assertEquals(entity.getEmpName(), "安保部合同管理");
    }

    @Test(enabled = true)
    public void findUserByPortalAccountId() throws Exception {
        UserEntity entity = aaaa.findUserByPortalAccountId("abbhtgl_jn");
        Assert.assertNotNull(entity);
        Assert.assertEquals(entity.getEmpName(), "安保部合同管理");
    }

    @Test(enabled = true)
    public void findUserListByUserName() throws Exception {
        List<UserEntity> list = aaaa.findUserListByUserName(137010062, "安保部合同管理");
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void checkUserIDAndOrgID() throws Exception {
        boolean result = aaaa.checkUserIDAndOrgID(3, 137010026);
        Assert.assertTrue(result);
    }

    @Test(enabled = true)
    public void findAccountListByUserID() throws Exception {
        List<AccountEntity> list = aaaa.findAccountListByUserID(1);
        Assert.assertTrue(list != null && list.size() > 0);
        list = aaaa.findAccountListByUserID(WRONG_ID);
        Assert.assertTrue(list == null || list.size() == 0);
    }

    @Test(enabled = true)
    public void findAccountByAccountID() throws Exception {
        AccountEntity entity = aaaa.findAccountByAccountID(1);
        Assert.assertNotNull(entity);
    }

    /*
     * @Test(enabled = true) public void findAcountByGroupID() throws Exception {
     * List<AccountEntity> list = aaaa.findAcountByGroupID(128); Assert.assertTrue(list != null &&
     * list.size() > 0); // list = aaaa.findAcountByGroupID(WRONG_ID); // Assert.assertTrue(list ==
     * null || list.size() == 0); }
     */

    @Test(enabled = true)
    public void findGroupByAccountID() throws Exception {
        List<GroupEntity> list = aaaa.findGroupListByAccountID(1);
        Assert.assertTrue(list != null && list.size() > 0);
        list = aaaa.findGroupListByAccountID(WRONG_ID);
        Assert.assertTrue(list == null || list.size() == 0);
    }

    @Test(enabled = true)
    public void findAllAccountListByAppResourceID() throws Exception {
        List<AccountEntity> list = aaaa.findAllAccountListByAppResourceID(1);
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findAllRoleListByAccountID() throws Exception {
        List<RoleEntity> list = aaaa.findAllRoleListByAccountID(1);
      
    }

    @Test(enabled = true)
    public void findAccountListByRoleID() throws Exception {
        // aaaa.assignRoleToAccount(1, 1);
        List<AccountEntity> list = aaaa.findAccountListByRoleID(1);
        Assert.assertTrue(list.size() > 0);
        list = aaaa.findAccountListByRoleID(WRONG_ID);
        Assert.assertTrue(list == null || list.size() == 0);

    }

    @Test(enabled = true)
    public void findRoleListByAppResourceID() throws Exception {
        List<RoleEntity> list = aaaa.findRoleListByAppResourceID(1);
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findRoleListByAccountResource() throws Exception {
        int accountId = 1;
        String resourcePath = "http://www.google.com";
        List<RoleEntity> list = aaaa.findRoleListByAccountResource(accountId, resourcePath);
        Assert.assertTrue(list != null && list.size() > 0);
        for (RoleEntity r : list) {
            System.out.println(JSONUtils.toString(r));
        }
    }

    @Test(enabled = true)
    public void findRoleListByDimensions() throws Exception {
        Map<String, Set<String>> queryParams = new HashMap<String, Set<String>>();

        Set<String> productId = new HashSet<String>();
        productId.add("1");

        Set<String> majorId = new HashSet<String>();
        majorId.add("1");

        Set<String> cloudOrgId = new HashSet<String>();
        // cloudOrgId.add("37158");

        Set<String> areaId = new HashSet<String>();

        Set<String> abstractRoleId = new HashSet<String>();
        // abstractRoleId.add("100222");

        Set<String> managementLine = new HashSet<String>();
        // managementLine.add("1");
        Set<String> serverSpecId = new HashSet<String>();
        serverSpecId.add("100222");
        queryParams.put("PRODUCT_ID", productId);
        queryParams.put("MAJOR_ID", majorId);
        queryParams.put("ORG_ID", cloudOrgId);
        queryParams.put("AREA_ID", areaId);
        queryParams.put("ABSTRACT_ROLE_ID", abstractRoleId);
        queryParams.put("MANAGEMENT_LINE_ID", managementLine);
        queryParams.put("SERVER_SPEC_ID", serverSpecId);
        List<RoleEntity> list = aaaa.findRoleListByDimensions(queryParams);
        System.out.println("find:" + list.size());
        for (RoleEntity r : list) {
            System.out.println(JSONUtils.toString(r));
        }
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findRoleDimensionsDataByRoleList() throws PaasAAAAException, PaasException {
        List<String> cloudRoleIdList = new ArrayList<String>();
        cloudRoleIdList.add("1");
        cloudRoleIdList.add("2");
        cloudRoleIdList.add("4270140");
        // cloudRoleIdList.add(null);
        // cloudRoleIdList.add("  ");

        List<RoleDimensionsData> res = aaaa.findRoleDimensionsDataByRoleList(cloudRoleIdList);
        Assert.assertTrue(res.size() > 0);
        System.out.println(JSONUtils.toString(res));
        // Assert.assertEquals(res.get(0).getData(DimensionEnum.AbstractRole).get(0),
        // "111");
    }

    @Test(enabled = true)
    public void findResourceListByAppIDResourceName() throws Exception {
        List<ResourceEntity> list = aaaa.findResourceListByAppIDResourceName("1", "1");
        Assert.assertTrue(list != null && list.size() > 0);
    }

    // @Test(enabled = true)
    // public void findRoleListByGroupID() throws Exception {
    // List<RoleEntity> list = aaaa.findRoleListByGroupID(1);
    // Assert.assertTrue(list != null && list.size() > 0);
    // }

    @Test(enabled = true)
    public void findResourceListByAccountID() throws Exception {
        List<ResourceEntity> list = aaaa.findResourceListByAccountID(1);
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findResourceListByRoleID() throws Exception {
        List<ResourceEntity> list = aaaa.findResourceListByRoleID(1);
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findResourceListByGroupID() throws Exception {
        List<ResourceEntity> list = aaaa.findResourceListByGroupID(1);
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findAppList() throws Exception {
        List<AppEntity> list = aaaa.findAppList();
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findResListByAppIDResType() throws Exception {
        List<ResourceEntity> resources = aaaa.findResListByAppIDResType(1, "1", "1");
        Assert.assertTrue(resources != null && resources.size() > 0);
        for (ResourceEntity r : resources) {
            System.out.println(JSONUtils.toString(r));
        }
    }

    @Test(enabled = true)
    public void findResourceListByDestAppId() throws Exception{
    	List<ResourceEntity> resources = aaaa.findResourceListByDestAppId("1");
        Assert.assertTrue(resources != null && resources.size() > 0);
        for (ResourceEntity r : resources) {
            System.out.println(JSONUtils.toString(r));
        }
    }
    
    @Test(enabled = true)
    public void findAppListByAccountId() throws Exception {
        List<AppEntityTest> list = aaaa.findAppListByAccount(1);
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findResListByParentResID() throws Exception {
        List<ResourceEntity> list = aaaa.findResListByParentResID(1);
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findRoleListByProductId() throws Exception {
        List<RoleEntity> list = aaaa.findRoleListByProductId("1");
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findRoleListByMajorId() throws Exception {
        List<RoleEntity> list = aaaa.findRoleListByMajorId("1");
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findRoleListByCloudOrgId() throws Exception {
        List<RoleEntity> list = aaaa.findRoleListByCloudOrgId(1);
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findRoleListByAreaId() throws Exception {
        String areaId = "1";
        List<RoleEntity> list = aaaa.findRoleListByAreaId(areaId);
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findRoleListByAbstractRoleId() throws Exception {
        int abstractRoleId = 1;
        List<RoleEntity> list = aaaa.findRoleListByAbstractRoleId(abstractRoleId);
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findAreaListByOrgId() throws Exception {
        List<String> list = aaaa.findAreaListByOrgId(110);
        Assert.assertTrue(list != null && list.size() > 0);
    }

    @Test(enabled = true)
    public void findAccountByPortalAccountID() throws Exception {
        AccountEntity accountEntity = aaaa.findAccountByPortalAccountID("1");
        Assert.assertNotNull(accountEntity);
    }

    @Test(enabled = true)
    public void findOrgsByOrgIdAndName() throws Exception {
        List<OrgEntity> entity = aaaa.findOrgsByOrgIdAndName(1, "公司");
       // List<OrgEntity> entity = aaaa.findOrgsByOrgIdAndName(11106, null);
        for(OrgEntity org:entity){
            System.out.println(JSONUtils.toString(org));
        }
        Assert.assertNotNull(entity);
        //List<OrgEntity> entity2 = aaaa.findOrgsByOrgIdAndName(110000180, "项目管理处");
       // Assert.assertNotNull(entity2);
    }

    @Test(enabled = true)
    public void findAllChildOrgsByOrgId() throws Exception {
        List<OrgEntity> entity = aaaa.findAllChildOrgsByOrgId(13300);
        for(OrgEntity org:entity){
            System.out.println(JSONUtils.toString(org));
        }
        System.out.println(entity.size());
        Assert.assertNotNull(entity);
    }
    测试Webdiff的速度
    public void findFavoriteGroup() throws Exception {
       
    	List<FavoriteGroup> entity = aaaa.findFavoriteGroup(0);
       
        Assert.assertNotNull(entity);
    }
    
    public void findFavoriteGroupMember() throws Exception {
        List<FavoriteGroupMember> entity = aaaa.findFavoriteGroupMember(2);
       
        Assert.assertNotNull(entity);
    }
    
    @Test(enabled = true)
    public void findUserEntityByCloudOrgIdAndEmpName() throws Exception {
    	
    	List<Object[]> obj = aaaa.findUserEntityByCloudOrgIdAndEmpName(135010014, "蔡善");
    	for(Object[] arr : obj){
    		UserEntity ue = (UserEntity) arr[0];
    		String orgName = (String) arr[1];
            System.out.println(JSONUtils.toString(ue));
            System.out.println(orgName);
        }
    	Assert.assertNotNull(obj);
    	
    }

    @Test(enabled = true)
    public void findFavoriteGroupAndMember() throws Exception{
    	List<FavoriteGroupAndMember> entity = aaaa.findFavoriteGroupAndMember(123456);
        for(FavoriteGroupAndMember org : entity){
            System.out.println(JSONUtils.toString(org));
        }
        Assert.assertNotNull(entity);
    }


    @Test(enabled = true)
    public void findAppListByAccountIdAndGroupId() throws Exception {
        List<AppEntity> list = aaaa.findAppListByAccountAndGroup(469926,"DB1DBAD7F609410EBAC228DE9128EB10");
        Assert.assertTrue(list != null && list.size() > 0);
        for (AppEntity o : list) {
            System.out.println(JSONUtils.toString(o));
        }
    }
    
    @Test(enabled = true)
    public void findCuncOrgByPartnerOrgId() throws Exception{
    	OrgEntity org = aaaa.findCuncOrgByPartnerOrgId(11);
    	 Assert.assertNotNull(org);
    	 System.out.println(JSONUtils.toString(org));
    }

}
