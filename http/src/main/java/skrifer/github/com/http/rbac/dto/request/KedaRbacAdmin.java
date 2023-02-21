package skrifer.github.com.http.rbac.dto.request;

import java.util.ArrayList;
import java.util.List;

public class KedaRbacAdmin {

    private String chargeTypeEnum = "PROJECT_SUPER_ADMIN";

    private String typeToId;

    private int status = 1;

    private List<ChargeRelationVO> chargeRelationVOList = new ArrayList<>();

    public boolean addchargeRelation(String userId, String targetId, String typeTo) {
        if (this.typeToId == null || "".equals(this.typeToId.trim())) {
            this.typeToId = targetId;
        }

        if (this.typeToId.equals(targetId) == false) {
            throw new RuntimeException("每次只能设置同一个项目的管理员");
        }

        ChargeRelationVO chargeRelationVO = new ChargeRelationVO();
        chargeRelationVO.setIdFrom(userId);
        chargeRelationVO.setIdTo(targetId);
        chargeRelationVO.setTypeTo(typeTo);
        return this.chargeRelationVOList.add(chargeRelationVO);
    }

    public boolean addchargeRelation(String userId, String targetId) {
        return addchargeRelation(userId, targetId, "CLIENT");
    }

    public String getChargeTypeEnum() {
        return chargeTypeEnum;
    }

    public void setChargeTypeEnum(String chargeTypeEnum) {
        this.chargeTypeEnum = chargeTypeEnum;
    }

    public String getTypeToId() {
        return typeToId;
    }

    public void setTypeToId(String typeToId) {
        this.typeToId = typeToId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ChargeRelationVO> getChargeRelationVOList() {
        return chargeRelationVOList;
    }

    public void setChargeRelationVOList(List<ChargeRelationVO> chargeRelationVOList) {
        this.chargeRelationVOList = chargeRelationVOList;
    }

    public static class ChargeRelationVO {
        private String typeFrom = "USER";
        private String idFrom;
        private String typeTo = "CLIENT";
        private String idTo;

        public String getTypeFrom() {
            return typeFrom;
        }

        public void setTypeFrom(String typeFrom) {
            this.typeFrom = typeFrom;
        }

        public String getIdFrom() {
            return idFrom;
        }

        public void setIdFrom(String idFrom) {
            this.idFrom = idFrom;
        }

        public String getTypeTo() {
            return typeTo;
        }

        public void setTypeTo(String typeTo) {
            this.typeTo = typeTo;
        }

        public String getIdTo() {
            return idTo;
        }

        public void setIdTo(String idTo) {
            this.idTo = idTo;
        }
    }

}
