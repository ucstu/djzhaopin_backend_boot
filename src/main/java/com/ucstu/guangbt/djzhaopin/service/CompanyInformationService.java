package com.ucstu.guangbt.djzhaopin.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ucstu.guangbt.djzhaopin.entity.company.CompanyInformation;
import com.ucstu.guangbt.djzhaopin.entity.user.DeliveryRecord;

import org.springframework.data.domain.Pageable;

public interface CompanyInformationService {
        public CompanyInformation appendCompanyInformation(CompanyInformation companyInformation);

        public CompanyInformation updateCompanyInformationByCompanyInfoId(UUID companyinfoid,
                        CompanyInformation companyInformation);

        public List<CompanyInformation> queryCompanyInformations(Pageable pageable);

        public CompanyInformation queryCompanyInformationByCompanyInfoId(UUID companyinfoid);

        public List<DeliveryRecord> queryDeliveryRecords(UUID companyinfoid, Integer state, Integer workingYears,
                        String sex, Integer age, UUID jobId, Date deliveryDate, String search, Pageable pageable);

}
