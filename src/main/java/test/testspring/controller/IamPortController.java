package test.testspring.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import test.testspring.domain.*;
import test.testspring.service.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/iamPort")
public class IamPortController {
    @Value("${imp_key}")
    private String imp_key;
    @Value("${imp_secret}")
    private String imp_secret;
    OrderService orderService;
    MemberService memberService;
    DeliveryService deliveryService;
    ProductService productService;

    CardService cardService;
    @Autowired
    public IamPortController(CardService cardService,OrderService orderService, MemberService memberService, DeliveryService deliveryService,ProductService productService){
        this.orderService = orderService;
        this.memberService = memberService;
        this.deliveryService = deliveryService;
        this.productService = productService;
        this.cardService = cardService;
    }

    @PostMapping("/payment")
    public ResponseEntity<?> postBook(@RequestBody Map<String, Object> model, HttpServletRequest req) throws Exception {
        //map, json 으로 받고 보내기
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        JSONObject responseObj = new JSONObject();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String buyerId = authentication.getName();
        BigDecimal money = new BigDecimal((Integer) model.get("paid_amount")); //돈
        String imp_uid = (String)model.get("imp_uid"); //고유번호
        String merchant_uid = (String)model.get("merchant_uid"); //주문번호
//        Long card_no = Long.parseLong((String)model.get("card_no"));
        Long product_no = Long.parseLong((String)model.get("product_no"));
        String sellerId = (String)model.get("seller_id");
        Long deliveryId = ((Integer) model.get("delivery_id")).longValue();
        int tradeAmount = Integer.parseInt((String)model.get("trade_amount"));

        boolean success = (Boolean)model.get("success");
        String error_msg = (String)model.get("error_msg");
        //결제성공했을시
        if(success==true) {
            try {
                Member buyer = memberService.getMemberById(buyerId);
                Member seller = memberService.getMemberById(sellerId);
                Delivery delivery = deliveryService.findById(deliveryId);
                Product product = productService.getProductByNo(product_no);
//            Card card = cardService.findCardById(card_no);

                Order order = Order.builder().imp_uid(imp_uid)
                        .merchant_uid(merchant_uid)
                        .total_price(money)
                        .seller(seller)
                        .buyer(buyer)
                        .delivery(delivery)
                        .product(product)
                        .trade_amount(tradeAmount)
//                    .card(card)
                        .build();
                //프론트단에서 보낸 금액과 아임포트에서 가져온 금액의 일치여부 검사
                IamportClient ic = new IamportClient(imp_key, imp_secret);
                IamportResponse<Payment> response = ic.paymentByImpUid(imp_uid);
                BigDecimal import_amount =response.getResponse().getAmount(); //api_amount
                if(money.equals(import_amount)) {
                    Order paymentResult = orderService.payment(order);
                    if(model.get("cid")!=null){
                        Long cid = Long.parseLong((String)model.get("cid"));
                        orderService.deleteCart(cid);
                    }
                    if(paymentResult.getTotal_price() == order.getTotal_price()) {
                        responseObj.put("process_result", "결제성공:" + paymentResult.getMerchant_uid());
                    }
                    else {
                        //불일치시 결제취소로직
                        ic.cancelPaymentByImpUid(new CancelData(merchant_uid, true));
                        responseObj.put("process_result", "결제에러:디비삽입실패");
                    }
                }else {
                    ic.cancelPaymentByImpUid(new CancelData(merchant_uid, true));
                    responseObj.put("process_result", "결제에러:금액불일치");
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {
            System.out.println("error_msg:" + error_msg);
            responseObj.put("process_result", "결제에러:" + error_msg);
        }
        System.out.println("responseObj:" + responseObj.toString());
        return new ResponseEntity<String>(responseObj.toString(), responseHeaders, HttpStatus.OK);
    }

    @PostMapping("/paymentByCart")
    public ResponseEntity<?> postBookInCarts(@RequestBody Map<String, Object> model, HttpServletRequest req) throws Exception {
        //map, json 으로 받고 보내기
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        JSONObject responseObj = new JSONObject();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String buyerId = authentication.getName();
        BigDecimal money = new BigDecimal((Integer) model.get("paid_amount")); //돈
        String imp_uid = (String)model.get("imp_uid"); //고유번호
        String merchant_uid = (String)model.get("merchant_uid"); //주문번호
//        Long card_no = Long.parseLong((String)model.get("card_no"));
        Long product_no = ((Integer)model.get("product_no")).longValue();
        String sellerId = (String)model.get("seller_id");
        Long deliveryId = ((Integer) model.get("delivery_id")).longValue();
        int tradeAmount = (Integer)model.get("trade_amount");

        boolean success = (Boolean)model.get("success");
        String error_msg = (String)model.get("error_msg");
        //결제성공했을시
        if(success==true) {
            try {
                Member buyer = memberService.getMemberById(buyerId);
                Member seller = memberService.getMemberById(sellerId);
                Delivery delivery = deliveryService.findById(deliveryId);
                Product product = productService.getProductByNo(product_no);
//            Card card = cardService.findCardById(card_no);

                Order order = Order.builder().imp_uid(imp_uid)
                        .merchant_uid(merchant_uid)
                        .total_price(money)
                        .seller(seller)
                        .buyer(buyer)
                        .delivery(delivery)
                        .product(product)
                        .trade_amount(tradeAmount)
//                    .card(card)
                        .build();
                //프론트단에서 보낸 금액과 아임포트에서 가져온 금액의 일치여부 검사
                IamportClient ic = new IamportClient(imp_key, imp_secret);
                IamportResponse<Payment> response = ic.paymentByImpUid(imp_uid);
                BigDecimal import_amount =response.getResponse().getAmount(); //api_amount
                if(money.equals(import_amount)) {
                    Order paymentResult = orderService.payment(order);
                    if(model.get("cid")!=null){
                        Long cid = ((Integer)model.get("cid")).longValue();
                        orderService.deleteCart(cid);
                    }
                    if(paymentResult.getTotal_price() == order.getTotal_price()) {
                        responseObj.put("process_result", "결제성공:" + paymentResult.getMerchant_uid());
                    }
                    else {
                        //불일치시 결제취소로직
                        ic.cancelPaymentByImpUid(new CancelData(merchant_uid, true));
                        responseObj.put("process_result", "결제에러:디비삽입실패");
                    }
                }else {
                    ic.cancelPaymentByImpUid(new CancelData(merchant_uid, true));
                    responseObj.put("process_result", "결제에러:금액불일치");
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {
            System.out.println("error_msg:" + error_msg);
            responseObj.put("process_result", "결제에러:" + error_msg);
        }
        return new ResponseEntity<String>(responseObj.toString(), responseHeaders, HttpStatus.OK);
    }


}