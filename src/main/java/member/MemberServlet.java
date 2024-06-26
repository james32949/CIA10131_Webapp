package member;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

public class MemberServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("application/json; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		
		MemberServlet ms = new MemberServlet();
		
		String action = req.getParameter("action");
		System.out.println(action); //測試接收參數
		

		switch(action){
		case "getAllMember":
			ms.getAllMember(req, res);
			break;
		case "checkState":
			ms.checkState(req, res);
			break;
		case "queryMember":
			ms.queryMember(req, res);
			break;
		case "userLogin":
			ms.userLogin(req, res);
			break;
		case "getUserInfomation":
			ms.getUserInfo(req, res);
			break;
		case "userLogout":
			ms.userLogout(req,res);
			break;
		}
		
	}

	private void userLogout(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		HttpSession session = req.getSession();
		session.removeAttribute("userId");
		session.removeAttribute("userAccount");
		
		JSONObject obj = new JSONObject();
		obj.put("AccountState","logout");
		res.getWriter().print(obj);
		return;
		
	}

	private void getUserInfo(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		HttpSession session = req.getSession();
		Integer memberId = (Integer) session.getAttribute("userId");	
		
		MemberService memSvc = new MemberService();
		MemberVO memVO =  memSvc.getOneMember(memberId);
		
		JSONObject obj = new JSONObject();
		obj.put("memberId", memVO.getMemberId());
		obj.put("memberName", memVO.getMemberName());
		obj.put("memberAccount", memVO.getMemberAccount());
		obj.put("memberEmail", memVO.getMemberEmail());
		obj.put("memberPhone", memVO.getMemberPhone());
		obj.put("memberAddress", memVO.getMemberAddress());
		obj.put("memberGender", memVO.getMemberGender());
		obj.put("memberBirthday", memVO.getMemberBirthday());
		obj.put("memberState", memVO.getMemberState());
		
//		System.out.println(obj);
		res.getWriter().print(obj);
		return;
	}
	//登入
	private void userLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		res.setContentType("application/json; charset=UTF-8");
		
		String inputAccount = req.getParameter("inputAccount").trim();
		String inputPassword = req.getParameter("inputPassword").trim();
		
//		System.out.println(inputAccount);
//		System.out.println(inputPassword);
		
		// 查詢資料庫資料
		MemberService memSvc = new MemberService();
		List<Object[]> list = memSvc.findByAccount(inputAccount);

		String memberPassword = "";
		Integer memberId = null;

		for (Object[] objs : list) {
			memberPassword = String.valueOf(objs[0]);
			memberId = (Integer) objs[1];
		}
//		System.out.println("資料庫密碼:"+memberPassword);
//		System.out.println("資料庫ID:"+memberId);


		// 檢查帳號
		if (memberPassword.isEmpty()) {
//			System.out.println("1:帳號或密碼錯誤");
			JSONObject obj = new JSONObject();
			obj.put("AccountState","error");
			res.getWriter().print(obj);
			return;

		} else if (memberPassword.equalsIgnoreCase(inputPassword)) {
//			System.out.println("登入成功");

			HttpSession session = req.getSession();
			session.setAttribute("userAccount", inputAccount);
			session.setAttribute("userId", memberId);

			
			JSONObject obj = new JSONObject();
			obj.put("AccountState","pass");
			res.getWriter().print(obj);
			return;


		} else {
//			System.out.println("2:帳號或密碼錯誤");
			JSONObject obj = new JSONObject();
			obj.put("AccountState","error");
			res.getWriter().print(obj);						
			return;
		}
		
		
	}

	private void queryMember(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		res.setContentType("application/json; charset=UTF-8");
		String inputName = req.getParameter("memberName").trim();
		String inputPhone = req.getParameter("memberPhone").trim();
		String inputEmail = req.getParameter("memberEmail").trim();
				
//		System.out.println(inputName);
//		System.out.println(inputPhone);
//		System.out.println(inputEmail);
		if(!inputName.isEmpty()) {
			
			MemberService memSvc = new MemberService();
			List<MemberVO> memberVO = memSvc.query(inputName, null, null);

			JSONArray objs = new JSONArray();
			
			for(MemberVO mem : memberVO) {
				JSONObject obj = new JSONObject();
				obj.put("memberId", mem.getMemberId());
				obj.put("memberName", mem.getMemberName());
				obj.put("memberAccount", mem.getMemberAccount());
				obj.put("memberEmail", mem.getMemberEmail());
				obj.put("memberPhone", mem.getMemberPhone());
				obj.put("memberAddress", mem.getMemberAddress());
				obj.put("memberGender", mem.getMemberGender());
				obj.put("memberBirthday", mem.getMemberBirthday());
				obj.put("memberState", mem.getMemberState());
				
				objs.put(obj);
			}
			
//			System.out.println(objs);
			res.getWriter().print(objs);
			
		}
		if(!inputPhone.isEmpty()) {
			
			MemberService memSvc = new MemberService();
			List<MemberVO> memberVO = memSvc.query(null, inputPhone, null);

			JSONArray objs = new JSONArray();
			
			for(MemberVO mem : memberVO) {
				JSONObject obj = new JSONObject();
				obj.put("memberId", mem.getMemberId());
				obj.put("memberName", mem.getMemberName());
				obj.put("memberAccount", mem.getMemberAccount());
				obj.put("memberEmail", mem.getMemberEmail());
				obj.put("memberPhone", mem.getMemberPhone());
				obj.put("memberAddress", mem.getMemberAddress());
				obj.put("memberGender", mem.getMemberGender());
				obj.put("memberBirthday", mem.getMemberBirthday());
				obj.put("memberState", mem.getMemberState());
				
				objs.put(obj);
			}
			
//			System.out.println(objs);
			res.getWriter().print(objs);

		}
		if(!inputEmail.isEmpty()) {
			
			MemberService memSvc = new MemberService();
			List<MemberVO> memberVO = memSvc.query(null, null, inputEmail);

			JSONArray objs = new JSONArray();
			
			for(MemberVO mem : memberVO) {
				JSONObject obj = new JSONObject();
				obj.put("memberId", mem.getMemberId());
				obj.put("memberName", mem.getMemberName());
				obj.put("memberAccount", mem.getMemberAccount());
				obj.put("memberEmail", mem.getMemberEmail());
				obj.put("memberPhone", mem.getMemberPhone());
				obj.put("memberAddress", mem.getMemberAddress());
				obj.put("memberGender", mem.getMemberGender());
				obj.put("memberBirthday", mem.getMemberBirthday());
				obj.put("memberState", mem.getMemberState());
				
				objs.put(obj);
			}
			
			System.out.println(objs);
			res.getWriter().print(objs);

		}
		
		
		
		
		
		return;
	}
	//會員狀態
	private void checkState(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		res.setContentType("application/json; charset=UTF-8");
		// --接收請求
		Integer memberId = Integer.valueOf( req.getParameter("memberId"));
		String memberState =  req.getParameter("memberState");
//		System.out.println("ID="+memberId);
//		System.out.println("State="+memberState);
		
		MemberVO memVO = new MemberVO();
		
		if("停權".equalsIgnoreCase(memberState)) { //--判斷帳號是否停權 2為停權
			memVO.setMemberState(0); // -復原帳號
		} else {
			memVO.setMemberState(2); // -帳號停權
		}
		
		// 修改資料
		MemberService memSvc = new MemberService();
		memVO = memSvc.upState(memVO.getMemberState(), memberId);
		
		// 轉交
		MemberVO oneMem = new MemberVO();		
		oneMem = memSvc.getOneMember(memberId);
		
		JSONObject obj = new JSONObject();
		obj.put("memberId", oneMem.getMemberId());
		obj.put("memberName", oneMem.getMemberName());
		obj.put("memberAccount", oneMem.getMemberAccount());
		obj.put("memberEmail", oneMem.getMemberEmail());
		obj.put("memberPhone", oneMem.getMemberPhone());
		obj.put("memberAddress", oneMem.getMemberAddress());
		obj.put("memberGender", oneMem.getMemberGender());
		obj.put("memberBirthday", oneMem.getMemberBirthday());
		obj.put("memberState", oneMem.getMemberState());
		
//		System.out.println(obj);
		res.getWriter().print(obj);
		return;
	}

	private void getAllMember(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		res.setContentType("application/json; charset=UTF-8");
		MemberService memSvc = new MemberService();
		List<MemberVO> memVO =  memSvc.getAll();
					
		JSONArray objs = new JSONArray();
		
		for(MemberVO mem : memVO) {
			JSONObject obj = new JSONObject();
			obj.put("memberId", mem.getMemberId());
			obj.put("memberName", mem.getMemberName());
			obj.put("memberAccount", mem.getMemberAccount());
			obj.put("memberEmail", mem.getMemberEmail());
			obj.put("memberPhone", mem.getMemberPhone());
			obj.put("memberAddress", mem.getMemberAddress());
			obj.put("memberGender", mem.getMemberGender());
			obj.put("memberBirthday", mem.getMemberBirthday());
			obj.put("memberState", mem.getMemberState());
			
			objs.put(obj);
		}
			
//		System.out.println(objs);
		res.getWriter().print(objs);
		
		
		return;
	}
}
