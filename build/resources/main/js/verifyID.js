      // function verifyPhone() {
      //   // 폼에 입력된 전화번호 가져오기
      //   const phoneNumber = document.getElementById("inputText").value;
      //   // 백엔드 API 호출
      //   $.ajax({
      //           url: "/findById",
      //           type: "POST",
      //           data: {'phone': phoneNumber},
      //           success: function(response) {
      //               document.querySelector('#codeBtn').addEventListener('click',function(){
      //                   let code = $('#code').val();
      //                   if(response==code){
      //                       window.location.pathname='findByPhone?phone='+phoneNumber;
      //                   }else error_text.val('일치하지 않습니다.');
      //               })          
      //           },
      //           error: function() {
      //               error_text.val("유효하지 않은 번호입니다!");
      //           }
      //       });
      // }
      // function verifyEmail() {
      // const email = document.getElementById("inputText").value;
      // // 백엔드 API 호출
      // $.ajax({
      //           url: "/findByEmail",
      //           type: "POST",
      //           data: {'email': email},
      //           success: function(response) {
      //               document.querySelector('#codeBtn').addEventListener('click',function(){
      //                   let code = $('#code').val();
      //                   if(response==code){
      //                     window.location.pathname='findByEmail?email='+email;
      //                   }else error_text.text('일치하지 않습니다.');
      //               })
  
      //           },
      //           error: function() {
      //             error_text.text("유효하지 않은 번호입니다!");
      //           }
      //       });
      // }

      // function verify(){
      //   let radios = document.getElementsByName('findMethod');
      //   let radioVal;
      //   for (let i = 0; i < radios.length; i++) {
      //     if (radios[i].checked) {
      //       radioVal = radios[i].value.trim();
      //       break;
      //     }
      //   }
      //   if (radioVal == 'email') {
      //       verifyEmail();
      //   } else if (radioVal == 'phone') {
      //       verifyPhone();
      //   }
      // }
      
      // function verifyId(){
      //   let radios = document.getElementsByName('findMethodPw');
      //   let radioVal;
      //   for (let i = 0; i < radios.length; i++) {
      //     if (radios[i].checked) {
      //       radioVal = radios[i].value.trim();
      //       break;
      //     }
      //   }
      //   if (radioVal == 'email') {
      //       verifyEmailSetPw();
      //   } else if (radioVal == 'phone') {
      //       verifyPhoneSetPw();
      //   }
      // }


      // function validId(){
      //   let id = $('#inputId').val().trim();
      //   let error_text = $('#error-textId');
      //   if (id === '') {
      //     error_text.text('아이디를 입력해주세요!');
      //     return false;
      //   } 

      //   let text =  '<p>이메일 또는 휴대폰 번호를 입력하세요.</p>' +
      //                           '<div class="form-check mb-3">' +
      //                           '<input type="radio" class="form-check-input" id="radioEmail" name="findMethodPw" value="email" checked>' +
      //                           '이메일로 찾기' +
      //                           '</div>' +
      //                           '<div class="form-check mb-3">' +
      //                           '<input type="radio" class="form-check-input" id="radioPhone" name="findMethodPw" value="phone">' +
      //                           '휴대폰번호로 찾기' +
      //                           '</div>' +
      //                           '<div class="mb-3 input-group">' +
      //                           '<input type="text" class="form-control" id="inputText2" aria-describedby="idHelp">' +
      //                           '<div class="input-group-append"><button type="button" class="btn btn-primary" value="아이디찾기" onclick="verifyId()">확인</button>' +
      //                           '</div>' +
      //                           '</div>';
      //   $('#pwModalBody').html(text);

      //   console.log(text);

        // $.ajax({
        //         url: "/findId",
        //         type: "POST",
        //         data: {'id': id},
        //         success: function(response) {

        //         },
        //         error: function() {
        //           error_text.text("해당하는 아이디가 없습니다!");
        //         }
        // });
      //}