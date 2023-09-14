package project.shop;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@SpringBootTest
public class DBTest {

  @TestConfiguration
  @RequiredArgsConstructor
  static class testConfig {
    private final EntityManager em;
    @Bean
    public MysqlRepository mysqlRepository() {
      return new MysqlRepository(em);
    }
  }

  @Autowired
  MysqlRepository mysqlRepository;

  @Test
  void insert() {
    Company company1 = new Company();
    company1.setCompanyName("삼성전자");
    Company company2 = new Company();
    company2.setCompanyName("LG 에너지솔루션");
    mysqlRepository.insertCompany(company1);
    mysqlRepository.insertCompany(company2);
  }

  @Test
  void find() {
    Company company1 = mysqlRepository.findCompany(1l);
    Company company2 = mysqlRepository.findCompany(2l);
    System.out.println("company1 = " + company1.getCompanyName());
    System.out.println("company2 = " + company2.getCompanyName());
  }

  @Test
  void delete() {
    Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> mysqlRepository.deleteCompany(3l));
  }

  @Test
  void update() {
    mysqlRepository.updateCompany(2l, "현대자동차");
    org.assertj.core.api.Assertions.assertThat(mysqlRepository.findCompany(2l).getCompanyName()).isEqualTo("현대자동차");
  }

  @RequiredArgsConstructor
  @Repository
  @Transactional
  static class MysqlRepository {
    private final EntityManager em;

    public void insertCompany(Company company) {
      em.persist(company);
    }

    public Company findCompany(Long id) {
      Company company = em.find(Company.class, id);
      return company;
    }

    public void deleteCompany(Long id) {
      Company company = findCompany(id);
      if (company == null) {
        throw new IllegalStateException("삭제할 데이터가 없음");
      }
      em.remove(company);
    }

    public void updateCompany(Long id, String companyName) {
      Company company = findCompany(id);
      company.setCompanyName(companyName);
    }
  }


  @Entity
  @Setter
  @Getter
  static class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;
    private String companyName;

    public Company() {
    }
  }


}
