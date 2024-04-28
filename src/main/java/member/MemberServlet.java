package member;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class MemberServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		
		String action = req.getParameter("action");
		System.out.println(action); //測試接收參數
		
		String forwardPath="";
		switch(action){
		case "getAllMember":
			forwardPath = getAllMember(req, res);
			break;
		case "checkState":
			forwardPath = checkState(req, res);
		}
		
	}

	private String checkState(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
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
		
		System.out.println(obj);
		res.getWriter().print(obj);
		return null;
	}

	private String getAllMember(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
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
			
		System.out.println(objs);
		res.getWriter().print(objs);
		
		
		return null;
	}
}