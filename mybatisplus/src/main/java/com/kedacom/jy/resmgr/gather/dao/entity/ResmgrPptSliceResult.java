package com.kedacom.jy.resmgr.gather.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * ppt切片结果信息
 * </p>
 *
 * @author shenjun
 * @since 2024-04-18
 */
@TableName("resmgr_ppt_slice_result")
@ApiModel(value = "ResmgrPptSliceResult对象", description = "ppt切片结果信息")
public class ResmgrPptSliceResult implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("主键")
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      @ApiModelProperty("ppt切片任务信息id")
      private Long pptSliceTaskId;

      @ApiModelProperty("任务描述")
      private String resultMsg;

      @ApiModelProperty("切片结果")
      private String resultInfo;

      @ApiModelProperty("课程唯一标识")
      private String courseCode;

      @ApiModelProperty("课程名称")
      private String courseName;

      @ApiModelProperty("上课时间yyyy-MM-dd HH:mm:ss")
      private String classBeginTime;

      @ApiModelProperty("下课时间yyyy-MM-dd HH:mm:ss")
      private String classOverTime;

      @ApiModelProperty("教师代码")
      private String lecturerCode;

      @ApiModelProperty("教师姓名")
      private String lecturerName;

      @ApiModelProperty("资源视频信息id")
      private Long resVideoId;

      @ApiModelProperty("资源视频信息ivs云录播")
      private Long resVideoIvsId;

      @ApiModelProperty("同步状态0-待同步;1-已同步")
      private Integer syncStatus;

      @ApiModelProperty("所属租户组织编号")
      private String tenantOrgCode;

      @ApiModelProperty("创建人")
      private String insertBy;

      @ApiModelProperty("创建时间")
      private LocalDateTime insertTime;

      @ApiModelProperty("更新人")
      private String updateBy;

      @ApiModelProperty("修改时间")
      private LocalDateTime updateTime;
    
    public Long getId() {
        return id;
    }

      public void setId(Long id) {
          this.id = id;
      }
    
    public Long getPptSliceTaskId() {
        return pptSliceTaskId;
    }

      public void setPptSliceTaskId(Long pptSliceTaskId) {
          this.pptSliceTaskId = pptSliceTaskId;
      }
    
    public String getResultMsg() {
        return resultMsg;
    }

      public void setResultMsg(String resultMsg) {
          this.resultMsg = resultMsg;
      }
    
    public String getResultInfo() {
        return resultInfo;
    }

      public void setResultInfo(String resultInfo) {
          this.resultInfo = resultInfo;
      }
    
    public String getCourseCode() {
        return courseCode;
    }

      public void setCourseCode(String courseCode) {
          this.courseCode = courseCode;
      }
    
    public String getCourseName() {
        return courseName;
    }

      public void setCourseName(String courseName) {
          this.courseName = courseName;
      }
    
    public String getClassBeginTime() {
        return classBeginTime;
    }

      public void setClassBeginTime(String classBeginTime) {
          this.classBeginTime = classBeginTime;
      }
    
    public String getClassOverTime() {
        return classOverTime;
    }

      public void setClassOverTime(String classOverTime) {
          this.classOverTime = classOverTime;
      }
    
    public String getLecturerCode() {
        return lecturerCode;
    }

      public void setLecturerCode(String lecturerCode) {
          this.lecturerCode = lecturerCode;
      }
    
    public String getLecturerName() {
        return lecturerName;
    }

      public void setLecturerName(String lecturerName) {
          this.lecturerName = lecturerName;
      }
    
    public Long getResVideoId() {
        return resVideoId;
    }

      public void setResVideoId(Long resVideoId) {
          this.resVideoId = resVideoId;
      }
    
    public Long getResVideoIvsId() {
        return resVideoIvsId;
    }

      public void setResVideoIvsId(Long resVideoIvsId) {
          this.resVideoIvsId = resVideoIvsId;
      }
    
    public Integer getSyncStatus() {
        return syncStatus;
    }

      public void setSyncStatus(Integer syncStatus) {
          this.syncStatus = syncStatus;
      }
    
    public String getTenantOrgCode() {
        return tenantOrgCode;
    }

      public void setTenantOrgCode(String tenantOrgCode) {
          this.tenantOrgCode = tenantOrgCode;
      }
    
    public String getInsertBy() {
        return insertBy;
    }

      public void setInsertBy(String insertBy) {
          this.insertBy = insertBy;
      }
    
    public LocalDateTime getInsertTime() {
        return insertTime;
    }

      public void setInsertTime(LocalDateTime insertTime) {
          this.insertTime = insertTime;
      }
    
    public String getUpdateBy() {
        return updateBy;
    }

      public void setUpdateBy(String updateBy) {
          this.updateBy = updateBy;
      }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

      public void setUpdateTime(LocalDateTime updateTime) {
          this.updateTime = updateTime;
      }

    @Override
    public String toString() {
        return "ResmgrPptSliceResult{" +
              "id = " + id +
                  ", pptSliceTaskId = " + pptSliceTaskId +
                  ", resultMsg = " + resultMsg +
                  ", resultInfo = " + resultInfo +
                  ", courseCode = " + courseCode +
                  ", courseName = " + courseName +
                  ", classBeginTime = " + classBeginTime +
                  ", classOverTime = " + classOverTime +
                  ", lecturerCode = " + lecturerCode +
                  ", lecturerName = " + lecturerName +
                  ", resVideoId = " + resVideoId +
                  ", resVideoIvsId = " + resVideoIvsId +
                  ", syncStatus = " + syncStatus +
                  ", tenantOrgCode = " + tenantOrgCode +
                  ", insertBy = " + insertBy +
                  ", insertTime = " + insertTime +
                  ", updateBy = " + updateBy +
                  ", updateTime = " + updateTime +
              "}";
    }
}
