package client;

import server.dao.ActivityTypeDao;
import server.entity.*;
import server.service.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;

public class CustomerPurchaseForm extends JFrame {
    private JPanel purchaseOptionsPanel;
    private JTextField brandName;
    private JTextField giftCardCode;
    private JTextField itemPurchased;
    private JButton goBackButton;
    private JButton purchaseButton;
    private JFrame jFrame;
    User user;

    public CustomerPurchaseForm() {
        purchaseButton.addActionListener(e -> {
            try {
                submit();
            } catch (ParseException | SQLException ex) {
                ex.printStackTrace();
            }
        });
        goBackButton.addActionListener(e -> {
            System.out.println("Going Back.");
            jFrame.setVisible(false);
        });
    }

    public void selectPurchase(User user) {
        this.user = user;
        jFrame = new JFrame("Customer: Purchase");
        jFrame.setPreferredSize(new Dimension(500, 500));
        jFrame.setResizable(false);
        jFrame.add(purchaseOptionsPanel);
        jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
    }

    private void submit() throws ParseException, SQLException {
        String activity_code;
        try {
            activity_code = new ActivityTypeDao().findActivityCodeByName("Purchase");
            System.out.println("activity code obtained " + activity_code);

            Brand brand = new BrandService().getBrandInfoByUserName(brandName.getText());
            LoyaltyProgram loyaltyProgram = new LoyaltyProgramService().fetchLoyaltyProgramByBrand(brand.getBrand_id());
            RewardEarningRules rewardEarningRules = new RewardEarningService().getReRulesByLoyaltyProgramActivityCode(loyaltyProgram, activity_code);
            System.out.println("Reward earning rule code obtained " + rewardEarningRules.getRewardEarningCode());

            Customer customer = new CustomerService().getCustomerInfoByUserName(user.getUserName());

            String giftCard = giftCardCode.getText();
            String item_Purchased = itemPurchased.getText();
            System.out.println("Item purchased " + item_Purchased);

            LoyaltyActivityLog loyaltyActivityLog = new LoyaltyActivityLog();
            loyaltyActivityLog.setActivity_code(activity_code);
            loyaltyActivityLog.setCustomer_id(customer.getId());
            loyaltyActivityLog.setReward_earning_code(rewardEarningRules.getRewardEarningCode());
            loyaltyActivityLog.setPoints_gained(rewardEarningRules.getRePoints());
            loyaltyActivityLog.setSummary(item_Purchased);

            LoyaltyActivityService loyaltyActivityService = new LoyaltyActivityService();
            String resp = loyaltyActivityService.createPurchase(loyaltyActivityLog);

            JOptionPane.showMessageDialog(this, resp);
            jFrame.setVisible(false);
        } catch (SQLException exception) {
            System.out.println("exception raised.");
            JOptionPane.showMessageDialog(this, "This activity is not supported by this brand");
            jFrame.setVisible(false);
        }
    }
}
