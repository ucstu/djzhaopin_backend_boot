package com.ucstu.guangbt.djzhaopin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ucstu.guangbt.djzhaopin.entity.company.CompanyInformation;
import com.ucstu.guangbt.djzhaopin.entity.company.position.PositionInformation;
import com.ucstu.guangbt.djzhaopin.entity.hr.HrInformation;
import com.ucstu.guangbt.djzhaopin.entity.user.DeliveryRecord;
import com.ucstu.guangbt.djzhaopin.entity.user.UserInformation;
import com.ucstu.guangbt.djzhaopin.entity.user.UserInspectionRecord;
import com.ucstu.guangbt.djzhaopin.model.PageResult;
import com.ucstu.guangbt.djzhaopin.model.ServiceToControllerBody;
import com.ucstu.guangbt.djzhaopin.model.company.BigData;
import com.ucstu.guangbt.djzhaopin.repository.MessageRecordRepository;
import com.ucstu.guangbt.djzhaopin.repository.company.CompanyInformationRepository;
import com.ucstu.guangbt.djzhaopin.repository.company.position.PositionInformationRepository;
import com.ucstu.guangbt.djzhaopin.repository.hr.HrInformationRepository;
import com.ucstu.guangbt.djzhaopin.repository.user.DeliveryRecordRepository;
import com.ucstu.guangbt.djzhaopin.repository.user.UserInspectionRecordRepository;
import com.ucstu.guangbt.djzhaopin.service.CompanyInformationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Predicate;

@Service
public class CompanyInformationServiceImpl implements CompanyInformationService {

    @Resource
    private CompanyInformationRepository companyInformationRepository;

    @Resource
    private PositionInformationRepository positionInformationRepository;

    @Resource
    private HrInformationRepository hrInformationRepository;

    @Resource
    private DeliveryRecordRepository deliveryRecordRepository;

    @Resource
    private UserInspectionRecordRepository userInspectionRecordRepository;

    @Resource
    private MessageRecordRepository messageRecordRepository;

    @Override
    @Transactional
    public ServiceToControllerBody<CompanyInformation> createCompanyInformation(CompanyInformation companyInformation) {
        ServiceToControllerBody<CompanyInformation> serviceToControllerBody = new ServiceToControllerBody<>();
        return serviceToControllerBody.created(companyInformationRepository.save(companyInformation));
    }

    @Override
    @Transactional
    public ServiceToControllerBody<CompanyInformation> deleteCompanyInformationByCompanyInfoId(
            UUID companyInformationId) {
        ServiceToControllerBody<CompanyInformation> serviceToControllerBody = new ServiceToControllerBody<>();
        Optional<CompanyInformation> companyInformationOptional = companyInformationRepository
                .findById(companyInformationId);
        if (!companyInformationOptional.isPresent()) {
            return serviceToControllerBody.error("companyInformationId", "公司信息不存在", companyInformationId);
        }
        companyInformationRepository.delete(companyInformationOptional.get());
        return serviceToControllerBody.success(companyInformationOptional.get());
    }

    @Override
    @Transactional
    public ServiceToControllerBody<CompanyInformation> updateCompanyInformationByCompanyInformationId(
            UUID companyInformationId, CompanyInformation companyInformation) {
        ServiceToControllerBody<CompanyInformation> serviceToControllerBody = new ServiceToControllerBody<>();
        Optional<CompanyInformation> companyInformationOptional = companyInformationRepository
                .findById(companyInformationId);
        if (!companyInformationOptional.isPresent()) {
            return serviceToControllerBody.error("companyInformationId", "公司信息不存在", companyInformationId);
        }
        companyInformation.setCompanyInformationId(companyInformationId);
        companyInformation.setCreatedAt(companyInformationOptional.get().getCreatedAt());
        companyInformation.setPositionInformations(companyInformationOptional.get().getPositionInformations());
        companyInformation.setAttentionRecords(companyInformationOptional.get().getAttentionRecords());
        companyInformation.setHrInformations(companyInformationOptional.get().getHrInformations());
        return serviceToControllerBody.success(companyInformationRepository.save(companyInformation));
    }

    @Override
    public ServiceToControllerBody<PageResult<CompanyInformation>> getCompanyInformations(
            String companyName, List<Integer> scales, List<Integer> financingStages, List<Integer> comprehensions,
            String location, Pageable pageable) {
        ServiceToControllerBody<PageResult<CompanyInformation>> serviceToControllerBody = new ServiceToControllerBody<>();
        Specification<CompanyInformation> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (companyName != null && !companyName.isEmpty()) {
                predicates.add(cb.like(root.get("companyName"), "%" + companyName + "%"));
            }
            if (scales != null && !scales.isEmpty()) {
                predicates.add(cb.in(root.get("scale")).value(scales));
            }
            if (financingStages != null && !financingStages.isEmpty()) {
                predicates.add(cb.in(root.get("financingStage")).value(financingStages));
            }
            if (comprehensions != null && !comprehensions.isEmpty()) {
                predicates.add(cb.in(root.get("comprehension")).value(comprehensions));
            }
            if (location != null && !location.isEmpty()) {
                Float longitude = Float.valueOf(location.split(",")[0]);
                Float latitude = Float.valueOf(location.split(",")[1]);
                Expression<Double> expression = cb.sqrt(cb.diff(
                        cb.prod(cb.diff(root.get("longitude"), longitude),
                                cb.diff(root.get("longitude"), longitude)),
                        cb.prod(cb.diff(root.get("latitude"), latitude),
                                cb.diff(root.get("latitude"), latitude))));
                query.orderBy(cb.asc(expression));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        Page<CompanyInformation> companyInformations = companyInformationRepository.findAll(specification, pageable);
        PageResult<CompanyInformation> pageResult = new PageResult<>();
        if (!companyInformations.hasContent()) {
            pageResult.setTotalCount(0);
            pageResult.setContents(new ArrayList<>());
            pageResult.setContentsName("companyInformations");
            return serviceToControllerBody.success(pageResult);
        }
        pageResult.setTotalCount(companyInformations.getTotalElements());
        pageResult.setContents(companyInformations.getContent());
        pageResult.setContentsName("companyInformations");
        return serviceToControllerBody.success(pageResult);
    }

    @Override
    public ServiceToControllerBody<CompanyInformation> getCompanyInformationByCompanyInformationId(
            UUID companyInformationId) {
        ServiceToControllerBody<CompanyInformation> serviceToControllerBody = new ServiceToControllerBody<>();
        Optional<CompanyInformation> companyInformationOptional = companyInformationRepository
                .findById(companyInformationId);
        if (!companyInformationOptional.isPresent()) {
            return serviceToControllerBody.error("companyInformationId", "公司信息不存在", companyInformationId);
        }
        return serviceToControllerBody.success(companyInformationOptional.get());
    }

    @Override
    public ServiceToControllerBody<PageResult<DeliveryRecord>> getDeliveryRecordsByCompanyInformationId(
            UUID companyInformationId, Date createdAt, Date updatedAt, List<Integer> status, Date interviewTime,
            List<Integer> workingYears, List<String> sexs, List<Integer> ages,
            List<UUID> positionInformationIds, List<Date> deliveryDates, String userName, Pageable pageable) {
        ServiceToControllerBody<PageResult<DeliveryRecord>> serviceToControllerBody = new ServiceToControllerBody<>();
        Optional<CompanyInformation> companyInformationOptional = companyInformationRepository
                .findById(companyInformationId);
        if (!companyInformationOptional.isPresent()) {
            return serviceToControllerBody.error("companyInformationId", "公司信息不存在", companyInformationId);
        }
        Specification<DeliveryRecord> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (createdAt != null) {
                predicates.add(cb.equal(root.get("createdAt"), createdAt));
            }
            if (updatedAt != null) {
                predicates.add(cb.equal(root.get("updatedAt"), updatedAt));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(root.get("status").in(status));
            }
            if (interviewTime != null) {
                predicates.add(cb.equal(root.get("interviewTime"), interviewTime));
            }
            Join<DeliveryRecord, UserInformation> userInformationJoin = root.join("userInformation");
            if (workingYears != null && !workingYears.isEmpty()) {
                predicates.add(userInformationJoin.get("workingYears").in(workingYears));
            }
            if (sexs != null && !sexs.isEmpty()) {
                predicates.add(userInformationJoin.get("sex").in(sexs));
            }
            if (ages != null && !ages.isEmpty()) {
                In<Integer> ageIn = cb.in(
                        cb.function(
                                "DATEDIFF",
                                Integer.class,
                                cb.literal("CURDATE()"),
                                userInformationJoin.get("dateOfBirth")));
                for (Integer age1 : ages) {
                    ageIn.value(age1);
                }
                predicates.add(ageIn);
            }
            Join<DeliveryRecord, PositionInformation> positionInformationJoin = root.join("positionInformation");
            if (positionInformationIds != null && !positionInformationIds.isEmpty()) {
                predicates.add(positionInformationJoin.get("positionInformationId").in(positionInformationIds));
            }
            if (deliveryDates != null && !deliveryDates.isEmpty()) {
                predicates.add(root.get("createdAt").in(deliveryDates));
            }
            if (userName != null) {
                predicates.add(cb.like(userInformationJoin.get("userName"), "%" + userName + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<DeliveryRecord> deliveryRecords = deliveryRecordRepository.findAll(specification, pageable);
        PageResult<DeliveryRecord> pageResult = new PageResult<>();
        if (!deliveryRecords.hasContent()) {
            pageResult.setTotalCount(0);
            pageResult.setContents(new ArrayList<>());
            pageResult.setContentsName("deliveryRecords");
            return serviceToControllerBody.success(pageResult);
        }
        pageResult.setTotalCount(deliveryRecords.getTotalElements());
        pageResult.setContents(deliveryRecords.getContent());
        pageResult.setContentsName("deliveryRecords");
        return serviceToControllerBody.success(pageResult);
    }

    @Override
    public ServiceToControllerBody<PageResult<PositionInformation>> getPositionInfos(String positionName,
            String salary, List<Integer> workingYears, List<Integer> educations, List<String> directionTags,
            String workProvinceName, String workCityName, List<String> workAreaNames, List<Integer> positionTypes,
            List<Integer> scales, List<Integer> financingStages, List<String> comprehensions, String workingPlace,
            Pageable pageable) {
        ServiceToControllerBody<PageResult<PositionInformation>> serviceToControllerBody = new ServiceToControllerBody<>();
        Specification<PositionInformation> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (positionName != null) {
                predicates.add(cb.like(root.get("positionName"), "%" + positionName + "%"));
            }
            if (salary != null) {
                String startingSalary = salary.split(",")[0];
                String ceilingSalary = salary.split(",")[1];
                predicates.add(cb.greaterThan(root.get("startingSalary"), startingSalary));
                predicates.add(cb.lessThan(root.get("ceilingSalary"), ceilingSalary));
            }
            if (workingYears != null && !workingYears.isEmpty()) {
                predicates.add(root.get("workingYears").in(workingYears));
            }
            if (educations != null && !educations.isEmpty()) {
                predicates.add(root.get("education").in(educations));
            }
            if (directionTags != null && !directionTags.isEmpty()) {
                ListJoin<PositionInformation, String> directionTagsJoin = root.joinList("directionTags");
                predicates.add(directionTagsJoin.in(directionTags));
            }
            if (workProvinceName != null) {
                predicates.add(cb.like(root.get("workProvinceName"), "%" + workProvinceName + "%"));
            }
            if (workCityName != null) {
                predicates.add(cb.like(root.get("workCityName"), "%" + workCityName + "%"));
            }
            if (workAreaNames != null && !workAreaNames.isEmpty()) {
                predicates.add(root.get("workAreaName").in(workAreaNames));
            }
            if (positionTypes != null && !positionTypes.isEmpty()) {
                predicates.add(root.get("positionType").in(positionTypes));
            }
            Join<PositionInformation, CompanyInformation> companyInformationJoin = root.join("companyInformation");
            if (scales != null && !scales.isEmpty()) {
                predicates.add(companyInformationJoin.get("scale").in(scales));
            }
            if (financingStages != null && !financingStages.isEmpty()) {
                predicates.add(companyInformationJoin.get("financingStage").in(financingStages));
            }
            if (comprehensions != null && !comprehensions.isEmpty()) {
                predicates.add(companyInformationJoin.get("comprehensionName").in(comprehensions));
            }
            if (workingPlace != null) {
                Float longitude = Float.valueOf(workingPlace.split(",")[0]);
                Float latitude = Float.valueOf(workingPlace.split(",")[1]);
                Expression<Double> expression = cb.sqrt(cb.diff(
                        cb.prod(cb.diff(root.get("longitude"), longitude),
                                cb.diff(root.get("longitude"), longitude)),
                        cb.prod(cb.diff(root.get("latitude"), latitude),
                                cb.diff(root.get("latitude"), latitude))));
                query.orderBy(cb.asc(expression));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        Page<PositionInformation> positionInformations = positionInformationRepository.findAll(specification, pageable);
        PageResult<PositionInformation> pageResult = new PageResult<>();
        if (!positionInformations.hasContent()) {
            pageResult.setTotalCount(0);
            pageResult.setContents(new ArrayList<>());
            pageResult.setContentsName("positionInformations");
            return serviceToControllerBody.success(pageResult);
        }
        pageResult.setTotalCount(positionInformations.getTotalElements());
        pageResult.setContents(positionInformations.getContent());
        pageResult.setContentsName("positionInformations");
        return serviceToControllerBody.success(pageResult);
    }

    @Override
    @Transactional
    public ServiceToControllerBody<PositionInformation> createPositionInformation(UUID companyInformationId,
            PositionInformation positionInformation) {
        ServiceToControllerBody<PositionInformation> serviceToControllerBody = new ServiceToControllerBody<>();
        Optional<CompanyInformation> companyInformationOptional = companyInformationRepository
                .findById(companyInformationId);
        Optional<HrInformation> hrInformationOptional = hrInformationRepository
                .findById(positionInformation.getHrInformationId());
        if (!companyInformationOptional.isPresent()) {
            return serviceToControllerBody.error("companyInformationId", "公司信息不存在", companyInformationId);
        }
        if (!hrInformationOptional.isPresent()) {
            return serviceToControllerBody.error("hrInformationId", "HR信息不存在",
                    positionInformation.getHrInformationId());
        }
        positionInformation.setCompanyInformation(companyInformationOptional.get());
        positionInformation.setHrInformation(hrInformationOptional.get());
        return serviceToControllerBody.created(positionInformationRepository.save(positionInformation));
    }

    @Override
    @Transactional
    public ServiceToControllerBody<PositionInformation> deletePositionInformationByPositionInformationId(
            UUID companyInformationId,
            UUID positionInformationId) {
        ServiceToControllerBody<PositionInformation> serviceToControllerBody = new ServiceToControllerBody<>();
        Optional<CompanyInformation> companyInformationOptional = companyInformationRepository
                .findById(companyInformationId);
        if (!companyInformationOptional.isPresent()) {
            return serviceToControllerBody.error("companyInformationId", "公司信息不存在", companyInformationId);
        }
        Optional<PositionInformation> positionInformationOptional = companyInformationOptional.get()
                .getPositionInformations().stream().filter(positionInformation -> positionInformation
                        .getPositionInformationId().equals(positionInformationId))
                .findFirst();
        if (!positionInformationOptional.isPresent()) {
            return serviceToControllerBody.error("positionInformationId", "职位信息不存在", positionInformationId);
        }
        positionInformationRepository.delete(positionInformationOptional.get());
        return serviceToControllerBody.success(positionInformationOptional.get());
    }

    @Override
    @Transactional
    public ServiceToControllerBody<PositionInformation> updatePositionInformationByPositionInformationId(
            UUID companyInformationId, UUID positionInformationId, PositionInformation positionInformation) {
        ServiceToControllerBody<PositionInformation> serviceToControllerBody = new ServiceToControllerBody<>();
        Optional<CompanyInformation> companyInformationOptional = companyInformationRepository
                .findById(companyInformationId);
        Optional<HrInformation> hrInformationOptional = hrInformationRepository
                .findById(positionInformation.getHrInformationId());
        if (!companyInformationOptional.isPresent()) {
            return serviceToControllerBody.error("companyInformationId", "公司信息不存在", companyInformationId);
        }
        if (!hrInformationOptional.isPresent()) {
            return serviceToControllerBody.error("hrInformationId", "HR信息不存在",
                    positionInformation.getHrInformationId());
        }
        Optional<PositionInformation> positionInformationOptional = companyInformationOptional.get()
                .getPositionInformations()
                .stream().filter(positionInformation1 -> positionInformation1.getPositionInformationId()
                        .equals(positionInformationId))
                .findFirst();
        if (!positionInformationOptional.isPresent()) {
            return serviceToControllerBody.error("positionInformationId", "职位信息不存在", positionInformationId);
        }
        positionInformation.setHrInformation(hrInformationOptional.get());
        positionInformation.setPositionInformationId(positionInformationId);
        positionInformation.setCompanyInformation(companyInformationOptional.get());
        positionInformation.setCreatedAt(positionInformationOptional.get().getCreatedAt());
        positionInformation.setDeliveryRecords(positionInformationOptional.get().getDeliveryRecords());
        positionInformation.setGarnerRecords(positionInformationOptional.get().getGarnerRecords());
        positionInformation.setUserInspectionRecords(positionInformationOptional.get().getUserInspectionRecords());
        return serviceToControllerBody.success(positionInformationRepository.save(positionInformation));
    }

    @Override
    public ServiceToControllerBody<PageResult<PositionInformation>> getPositionInformationsByCompanyInformationId(
            UUID companyInformationId, String positionName, String salary, List<Integer> workingYears,
            List<Integer> educations, List<String> directionTags, String workProvinceName, String workCityName,
            List<String> workAreaNames, List<Integer> positionTypes, List<Integer> scales,
            List<Integer> financingStages, List<String> comprehensions, String workingPlace, Pageable pageable) {
        ServiceToControllerBody<PageResult<PositionInformation>> serviceToControllerBody = new ServiceToControllerBody<>();
        Optional<CompanyInformation> companyInformationOptional = companyInformationRepository
                .findById(companyInformationId);
        if (!companyInformationOptional.isPresent()) {
            return serviceToControllerBody.error("companyInformationId", "公司信息不存在", companyInformationId);
        }
        Specification<PositionInformation> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (positionName != null) {
                predicates.add(cb.like(root.get("positionName"), "%" + positionName + "%"));
            }
            if (salary != null) {
                String startingSalary = salary.split(",")[0];
                String ceilingSalary = salary.split(",")[1];
                predicates.add(cb.greaterThan(root.get("startingSalary"), startingSalary));
                predicates.add(cb.lessThan(root.get("ceilingSalary"), ceilingSalary));
            }
            if (workingYears != null && !workingYears.isEmpty()) {
                predicates.add(root.get("workingYears").in(workingYears));
            }
            if (educations != null && !educations.isEmpty()) {
                predicates.add(root.get("education").in(educations));
            }
            if (directionTags != null && !directionTags.isEmpty()) {
                ListJoin<PositionInformation, String> directionTagsJoin = root.joinList("directionTags");
                predicates.add(directionTagsJoin.in(directionTags));
            }
            if (workProvinceName != null) {
                predicates.add(cb.like(root.get("workProvinceName"), "%" + workProvinceName + "%"));
            }
            if (workCityName != null) {
                predicates.add(cb.like(root.get("workCityName"), "%" + workCityName + "%"));
            }
            if (workAreaNames != null && !workAreaNames.isEmpty()) {
                ListJoin<PositionInformation, String> workAreaNamesJoin = root.joinList("workAreaNames");
                predicates.add(workAreaNamesJoin.in(workAreaNames));
            }
            if (positionTypes != null && !positionTypes.isEmpty()) {
                predicates.add(root.get("positionType").in(positionTypes));
            }
            Join<PositionInformation, CompanyInformation> companyInformationJoin = root.join("companyInformation");
            if (scales != null && !scales.isEmpty()) {
                predicates.add(companyInformationJoin.get("scale").in(scales));
            }
            if (financingStages != null && !financingStages.isEmpty()) {
                predicates.add(companyInformationJoin.get("financingStage").in(financingStages));
            }
            if (comprehensions != null && !comprehensions.isEmpty()) {
                predicates.add(companyInformationJoin.get("comprehensionName").in(comprehensions));
            }
            if (workingPlace != null) {
                Float longitude = Float.valueOf(workingPlace.split(",")[0]);
                Float latitude = Float.valueOf(workingPlace.split(",")[1]);
                Expression<Double> expression = cb.sqrt(cb.diff(
                        cb.prod(cb.diff(root.get("longitude"), longitude),
                                cb.diff(root.get("longitude"), longitude)),
                        cb.prod(cb.diff(root.get("latitude"), latitude),
                                cb.diff(root.get("latitude"), latitude))));
                query.orderBy(cb.asc(expression));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        Page<PositionInformation> positionInformations = positionInformationRepository.findAll(specification, pageable);
        PageResult<PositionInformation> pageResult = new PageResult<>();
        if (!positionInformations.hasContent()) {
            pageResult.setTotalCount(0);
            pageResult.setContents(new ArrayList<>());
            pageResult.setContentsName("positionInformations");
            return serviceToControllerBody.success(pageResult);
        }
        pageResult.setTotalCount(positionInformations.getTotalElements());
        pageResult.setContents(positionInformations.getContent());
        pageResult.setContentsName("positionInformations");
        return serviceToControllerBody.success(pageResult);
    }

    @Override
    public ServiceToControllerBody<PositionInformation> getPositionInformationByPositionInformationId(
            UUID companyInformationId,
            UUID positionInformationId) {
        ServiceToControllerBody<PositionInformation> serviceToControllerBody = new ServiceToControllerBody<>();
        Optional<CompanyInformation> companyInformationOptional = companyInformationRepository
                .findById(companyInformationId);
        if (!companyInformationOptional.isPresent()) {
            return serviceToControllerBody.error("companyInformationId", "公司信息不存在", companyInformationId);
        }
        Optional<PositionInformation> positionInformationOptional = companyInformationOptional.get()
                .getPositionInformations()
                .stream().filter(positionInformation -> positionInformation.getPositionInformationId()
                        .equals(positionInformationId))
                .findFirst();
        if (!positionInformationOptional.isPresent()) {
            return serviceToControllerBody.error("positionInformationId", "职位信息不存在", positionInformationId);
        }
        return serviceToControllerBody.success(positionInformationOptional.get());
    }

    @Override
    public ServiceToControllerBody<PageResult<UserInspectionRecord>> getSawMeRecordsByCompanyInformationId(
            UUID companyInformationId, Date startDate, Date endDate, Pageable pageable) {
        ServiceToControllerBody<PageResult<UserInspectionRecord>> serviceToControllerBody = new ServiceToControllerBody<>();
        Optional<CompanyInformation> companyInformationOptional = companyInformationRepository
                .findById(companyInformationId);
        if (!companyInformationOptional.isPresent()) {
            return serviceToControllerBody.error("companyInformationId", "公司信息不存在", companyInformationId);
        }
        Page<UserInspectionRecord> userInspectionRecords = userInspectionRecordRepository
                .findByCompanyInformationAndCreatedAtBetween(companyInformationOptional.get(), startDate, endDate,
                        pageable);
        PageResult<UserInspectionRecord> pageResult = new PageResult<>();
        if (!userInspectionRecords.hasContent()) {
            pageResult.setTotalCount(0);
            pageResult.setContents(new ArrayList<>());
            pageResult.setContentsName("userInspectionRecords");
            return serviceToControllerBody.success(pageResult);
        }
        pageResult.setTotalCount(userInspectionRecords.getTotalElements());
        pageResult.setContents(userInspectionRecords.getContent());
        pageResult.setContentsName("userInspectionRecords");
        return serviceToControllerBody.success(pageResult);
    }

    @Override
    public ServiceToControllerBody<List<BigData>> getBigDataByCompanyInformationId(UUID companyInformationId,
            UUID hrInformationId, Date startDate, Date endDate, Pageable pageable) {
        ServiceToControllerBody<List<BigData>> serviceToControllerBody = new ServiceToControllerBody<>();
        Optional<CompanyInformation> companyInformationOptional = companyInformationRepository
                .findById(companyInformationId);
        if (!companyInformationOptional.isPresent()) {
            return serviceToControllerBody.error("companyInformationId", "公司信息不存在", companyInformationId);
        }
        List<BigData> bigDatas = new ArrayList<>();
        while (startDate.before(endDate)) {
            BigData bigData = new BigData();
            bigData.setDate(startDate);
            bigData.setInspectionRecordCount(userInspectionRecordRepository
                    .countByCompanyInformationAndCreatedAt(companyInformationOptional.get(), startDate));
            bigData.setDeliveryRecordCount(deliveryRecordRepository
                    .countByCompanyInformationAndCreatedAt(companyInformationOptional.get(), startDate));
            bigData.setOnlineCommunicateCount(
                    messageRecordRepository.countByServiceIdAndCreatedAt(hrInformationId,
                            startDate));
            bigDatas.add(bigData);
        }
        return serviceToControllerBody.success(bigDatas);
    }

}
