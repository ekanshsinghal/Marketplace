package server.service;

import server.dao.BrandDao;
import server.entity.Brand;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BrandService {

    private BrandDao brandDao;
    public BrandService() {
    }

    public String createBrand(String brandNameText, String brandAddressText,
                              String brandJoinDateText, String brandCreatorIdText, String usernameText) throws ParseException, SQLException {

        brandDao = new BrandDao();
        Brand brand = new Brand();

        // todo: do all validations here
        brand.setBrand_name(brandNameText);
        brand.setAddress(brandAddressText);
        brand.setJoindate(convertStringToDate(brandJoinDateText));
        brand.setUsername(usernameText);
        brand.setCreatedBy(brandCreatorIdText);
        brand.setCreatedAt(new Date());
        brand.setUpdatedBy(brandCreatorIdText);

        String response = brandDao.createBrand(brand);
        return response;
    }

    public Date convertStringToDate(String dateString) throws ParseException {
        Date date = new SimpleDateFormat("MM/dd/yyyy").parse(dateString);
        return date;
    }

    public Brand getBrandInfoByUserName(String brandUserNameText) throws SQLException {
        Brand brand = new Brand();
        brandDao = new BrandDao();
        brand.setUsername(brandUserNameText);

        brand = brandDao.findBrandInfoByUserName(brand);
        return brand;
    }
}
