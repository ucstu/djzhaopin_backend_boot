package com.ucstu.guangbt.djzhaopin.service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.ucstu.guangbt.djzhaopin.entity.user.AttentionRecord;
import com.ucstu.guangbt.djzhaopin.entity.user.DeliveryRecord;
import com.ucstu.guangbt.djzhaopin.entity.user.EducationExperience;
import com.ucstu.guangbt.djzhaopin.entity.user.GarnerRecord;
import com.ucstu.guangbt.djzhaopin.entity.user.InspectionRecord;
import com.ucstu.guangbt.djzhaopin.entity.user.JobExpectation;
import com.ucstu.guangbt.djzhaopin.entity.user.ProjectExperience;
import com.ucstu.guangbt.djzhaopin.entity.user.UserInformation;
import com.ucstu.guangbt.djzhaopin.entity.user.WorkExperience;

import org.springframework.data.domain.Pageable;

public interface UserInformationService {

        public Optional<UserInformation> updateUserInformationByUserInfoId(UUID userinfoid,
                        UserInformation userInformation);

        public Stream<UserInformation> getUserInformations(Pageable pageable);

        public Optional<UserInformation> getUserInformationByUserInfoId(UUID userinfoid);

        public Optional<JobExpectation> createJobExpectation(UUID userinfoid, JobExpectation jobExpectation);

        public Optional<JobExpectation> deleteJobExpectationByJobExpectationId(UUID userinfoid, UUID jobexpectationid);

        public Optional<JobExpectation> updateJobExpectationByJobExpectationId(UUID userinfoid, UUID jobexpectationid,
                        JobExpectation jobExpectation);

        public Stream<JobExpectation> getJobExpectations(UUID userinfoid);

        public Optional<JobExpectation> getJobExpectationByJobExpectationId(UUID userinfoid, UUID jobexpectationid);

        public Optional<EducationExperience> createEducationExperience(UUID userinfoid,
                        EducationExperience educationExperience);

        public Optional<EducationExperience> deleteEducationExperienceByEducationExperienceId(UUID userinfoid,
                        UUID eduexperienceid);

        public Optional<EducationExperience> updateEducationExperienceByEducationExperienceId(UUID userinfoid,
                        UUID eduexperienceid,
                        EducationExperience educationExperience);

        public Stream<EducationExperience> getEducationExperiences(UUID userinfoid);

        public Optional<EducationExperience> getEducationExperienceByEducationExperienceId(UUID userinfoid,
                        UUID eduexperienceid);

        public Optional<WorkExperience> createWorkExperience(UUID userinfoid, WorkExperience workExperience);

        public Optional<WorkExperience> deleteWorkExperienceByWorkExperienceId(UUID userinfoid, UUID workexperienceid);

        public Optional<WorkExperience> updateWorkExperienceByWorkExperienceId(UUID userinfoid, UUID workexperienceid,
                        WorkExperience workExperience);

        public Stream<WorkExperience> getWorkExperiences(UUID userinfoid);

        public Optional<WorkExperience> getWorkExperienceByWorkExperienceId(UUID userinfoid, UUID workexperienceid);

        public Optional<ProjectExperience> createProjectExperience(UUID userinfoid,
                        ProjectExperience projectExperience);

        public Optional<ProjectExperience> deleteProjectExperienceByProjectExperienceId(UUID userinfoid,
                        UUID projectexperienceid);

        public Optional<ProjectExperience> updateProjectExperienceByProjectExperienceId(UUID userinfoid,
                        UUID projectexperienceid,
                        ProjectExperience projectExperience);

        public Stream<ProjectExperience> getProjectExperiences(UUID userinfoid);

        public Optional<ProjectExperience> getProjectExperienceByProjectExperienceId(UUID userinfoid,
                        UUID projectexperienceid);

        public Optional<DeliveryRecord> createDeliveryRecord(UUID userinfoid, DeliveryRecord deliveryRecord);

        public Optional<DeliveryRecord> deleteDeliveryRecordByDeliveryRecordId(UUID userinfoid, UUID deliveryrecordid);

        public Optional<DeliveryRecord> updateDeliveryRecordByDeliveryRecordId(UUID userinfoid, UUID deliveryrecordid,
                        DeliveryRecord deliveryRecord);

        public Stream<DeliveryRecord> getDeliveryRecords(UUID userinfoid);

        public Optional<DeliveryRecord> getDeliveryRecordByDeliveryRecordId(UUID userinfoid, UUID deliveryrecordid);

        public Optional<AttentionRecord> createAttentionRecord(UUID userinfoid, AttentionRecord attentionRecord);

        public Optional<AttentionRecord> deleteAttentionRecordByAttentionRecordId(UUID userinfoid,
                        UUID attentionrecordid);

        public Optional<AttentionRecord> updateAttentionRecordByAttentionRecordId(UUID userinfoid,
                        UUID attentionrecordid,
                        AttentionRecord attentionRecord);

        public Stream<AttentionRecord> getAttentionRecords(UUID userinfoid);

        public Optional<AttentionRecord> getAttentionRecordByAttentionRecordId(UUID userinfoid, UUID attentionrecordid);

        public Optional<InspectionRecord> createInspectionRecord(UUID userinfoid, InspectionRecord inspectionRecord);

        public Optional<InspectionRecord> deleteInspectionRecordByInspectionRecordId(UUID userinfoid,
                        UUID inspectionrecordid);

        public Optional<InspectionRecord> updateInspectionRecordByInspectionRecordId(UUID userinfoid,
                        UUID inspectionrecordid,
                        InspectionRecord inspectionRecord);

        public Stream<InspectionRecord> getInspectionRecords(UUID userinfoid);

        public Optional<InspectionRecord> getInspectionRecordByInspectionRecordId(UUID userinfoid,
                        UUID inspectionrecordid);

        public Optional<GarnerRecord> createGarnerRecord(UUID userinfoid, GarnerRecord garnerRecord);

        public Optional<GarnerRecord> deleteGarnerRecordByGarnerRecordId(UUID userinfoid, UUID garnerrecordid);

        public Optional<GarnerRecord> updateGarnerRecordByGarnerRecordId(UUID userinfoid, UUID garnerrecordid,
                        GarnerRecord garnerRecord);

        public Stream<GarnerRecord> getGarnerRecords(UUID userinfoid);

        public Optional<GarnerRecord> getGarnerRecordByGarnerRecordId(UUID userinfoid, UUID garnerrecordid);

}
