package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import enums.PaymentMethods;
import validator.Validador;
import models.Billing;
import models.Company;
import models.Invoice;
import models.Payment;
import models.PrePaid;
import models.Time;
import models.TimeTable;
import models.User;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.data.format.Formats.NonEmpty;
import play.data.format.Formatters;
import play.data.validation.Constraints.Required;
import play.i18n.MessagesApi;
import play.mvc.*;

import views.html.*;
import play.libs.Json;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import utility.Password;

public class HomeController extends Controller {

	@Inject
	MailerClient mailerClient;

	MessagesApi msgApi = new MessagesApi(null);
	@Inject
	FormFactory formFactory = new FormFactory(msgApi, new Formatters(msgApi), new Validador());

	// TODO
	// make this a utility class
	public void sendEmail(String emailadress) {
		String cid = "1234";
		Email email = new Email();
		email.setSubject("Account Verification");
		email.setFrom("playappofefe@yahoo.com");
		email.addTo(emailadress);
		String hashed = Password.hashPassword(emailadress);
		String urlsafe = hashed.replaceAll("/", "-");
		email.setBodyText(
				"Click to this link to verify your account: https://appofefe.herokuapp.com/validate/" + urlsafe);
		mailerClient.send(email);
	}

	/*
	 * public Result newUser() { Form<User> userForm =
	 * formFactory.form(User.class).bindFromRequest(); if (userForm.hasErrors())
	 * { return badRequest(views.html.main.render(userForm)); } else { if
	 * (User.find.byId(userForm.get().email) == null) { String pass =
	 * userForm.get().password; if (passwordChecker(pass)) { return
	 * badRequest(views.html.main.render(userForm)); } else { User user =
	 * userForm.get(); sendEmail(user.email); User.create(user);
	 * User.createTableToUser(user); String cName = user.comName; Company c =
	 * Company.find.byId(cName); if (c == null) { Company com = new
	 * Company(cName); Company.createCompany(com); com.addUser(user);
	 * com.save(); user.company = com; } else { c.addUser(user); c.save();
	 * user.company = c; }
	 * 
	 * user.save(); flash("success",
	 * "You've have succesfully created an account.To be able to login, please verify your email."
	 * ); return ok(views.html.login.render(formFactory.form(Login.class))); } }
	 * else { flash("failure", "Account already exists"); return
	 * badRequest(views.html.main.render(userForm)); } } }
	 */

	@Security.Authenticated(Secured.class)
	public Result getPaymentInfo() {
		User user = User.find.byId(request().username());
		Payment payment = user.company.payment;
		if (payment != null) {
			if (payment.method == PaymentMethods.Billing) {
				return ok(Json.toJson(user.company.payment.billing));
			} else if (payment.method == PaymentMethods.Prepaid) {
				return ok(Json.toJson(user.company.payment.prepaid));
			} else {
				return ok(Json.toJson(user.company.payment));
			}
		} else {
			ObjectNode response = Json.newObject();
			response.put("Error", "No payment method selected.");
			return ok(response);
		}
	}

	@Security.Authenticated(Secured.class)
	public Result selectPaymentMethod() {

		JsonNode json = request().body().asJson();
		User user = User.find.byId(request().username());
		if (user.company.payment == null) {
			if (json.findPath("method").textValue() == null) {
				ObjectNode response = Json.newObject();
				response.put("Error", "Invalid Json format.");
				return ok(response);
			} else {
				String method = json.findPath("method").textValue();
				String subs = json.findPath("subscription").textValue();
				Payment payment = new Payment(subs);
				if (method.equalsIgnoreCase("prepaid")) {
					payment.method = PaymentMethods.Prepaid;
					payment.save();
					String periodType = json.findPath("periodType").textValue();
					int periodAmount = Integer.parseInt(json.findPath("periodAmount").textValue());
					LocalDate start = new LocalDate();
					LocalDate end = new LocalDate();
					if (periodType == "monthly") {
						end.plusMonths(periodAmount);
					} else {
						end.plusWeeks(periodAmount);
					}
					PrePaid prepaid = new PrePaid(start, end);
					prepaid.payment = payment;
					prepaid.save();
					payment.prepaid = prepaid;
				} else if (method.equalsIgnoreCase("billing")) {
					payment.method = PaymentMethods.Billing;
					payment.save();
					String recurr = json.findPath("recurrence").textValue();
					LocalDate start = new LocalDate();
					Billing billing = new Billing(start, recurr);
					billing.payment = payment;
					billing.save();
					payment.billing = billing;

				} else if (method.equalsIgnoreCase("free")) {
					payment.method = PaymentMethods.Free;
					payment.callLimit = 1000;

				} else {
					ObjectNode response = Json.newObject();
					response.put("Error", "Not a valid method option.");
					return ok(response);
				}
				payment.save();
				user.company.payment = payment;
				payment.company = user.company;
				payment.save();
				user.company.save();
				ObjectNode response = Json.newObject();
				response.put("Success", "Payment method is set to '" + method + "'.");
				return ok(response);

			}

		} else {
			ObjectNode response = Json.newObject();
			response.put("Error", "A payment method is already set to this company.");
			return ok(response);
		}
		/*
		 * User user = User.find.byId(id); Form<Payment> methodForm =
		 * formFactory.form(Payment.class).bindFromRequest(); Payment payment =
		 * new Payment(); payment.save(); if
		 * (methodForm.get().method.equals("P")) { PrePaid prepaid = new
		 * PrePaid(); prepaid.payment = payment; prepaid.save(); payment.prepaid
		 * = prepaid; } else { Billing billing = new Billing(); billing.payment
		 * = payment; billing.save(); payment.billing = billing; }
		 * payment.save(); user.company.payment = payment; payment.company =
		 * user.company; payment.save(); user.company.save(); return
		 * redirect(routes.HomeController.home());
		 */
	}
	/*
	 * public Result addUser(String id) { Form<User> userForm =
	 * formFactory.form(User.class).bindFromRequest(); if (userForm.hasErrors())
	 * { return badRequest(views.html.home.render(User.find.all(),
	 * User.find.byId(id), userForm, formFactory.form(Payment.class))); } else {
	 * if (User.find.byId(userForm.get().email) == null) { String pass =
	 * userForm.get().password; if (passwordChecker(pass)) { return
	 * badRequest(views.html.home.render(User.find.all(), User.find.byId(id),
	 * userForm, formFactory.form(Payment.class))); } else { User user =
	 * userForm.get(); sendEmail(user.email); User.create(user);
	 * User.createTableToUser(user); String cName = user.comName; Company c =
	 * Company.find.byId(cName); if (c == null) { Company com = new
	 * Company(cName); com.addUser(user); com.save(); user.company = com; } else
	 * { c.addUser(user); c.save(); user.company = c; } user.save();
	 * flash("success", "You've have succesfully created an account."); return
	 * redirect(routes.HomeController.home()); } } else { flash("failure",
	 * "Account already exists"); return
	 * badRequest(views.html.home.render(User.find.all(), User.find.byId(id),
	 * userForm, formFactory.form(Payment.class))); } } }
	 */
	/*
	 * @Security.Authenticated(Secured.class) public Result home() { Form<User>
	 * userForm = formFactory.form(User.class).bindFromRequest(); User mainuser
	 * = User.find.byId(request().username()); return
	 * ok(home.render(User.find.all(), mainuser, userForm,
	 * formFactory.form(Payment.class))); }
	 */

	@Security.Authenticated(Secured.class)
	public Result getAllUsers() {
		JsonNode request = request().body().asJson();
		List<User> list = User.find.all();
		return ok(Json.toJson(list));
	}

	public Result newUser() {
		JsonNode json = request().body().asJson();
		if (json.findPath("email").textValue() == null || json.findPath("name").textValue() == null
				|| json.findPath("comName").textValue() == null || json.findPath("password").textValue() == null) {
			ObjectNode response = Json.newObject();
			response.put("Error", "Missing parameters in the Json.");
			return ok(response);
		}
		if (User.find.byId(json.findPath("email").textValue()) == null) {
			User user = new User(json.findPath("email").textValue(), json.findPath("name").textValue(),
					json.findPath("password").textValue());
			if (json == null) {
				ObjectNode response = Json.newObject();
				response.put("Error", "Expecting Json data!");
				return ok(response);
			} else {

				if (user.name == null || user.password == null || user.email == null) {
					ObjectNode response = Json.newObject();
					response.put("Error", "Invalid Json format!");
					return ok(response);
				} else {
					sendEmail(user.email);
					User.create(user);
					User.createTableToUser(user);
					String cName = json.findPath("comName").textValue();
					Company c = Company.find.byId(cName);
					if (c == null) {
						Company com = new Company(cName);
						com.addUser(user);
						com.save();
						user.company = com;
					} else {
						c.addUser(user);
						c.save();
						user.company = c;
					}
					user.save();
					ObjectNode response = Json.newObject();
					response.put("Success",
							"User created succesfully, a verification link has been sent to your email.");
					return ok(response);
				}
			}
		} else {
			ObjectNode response = Json.newObject();
			response.put("Error", "User already exists.");
			return ok(response);
		}
	}

	@Security.Authenticated(Secured.class)
	public Result deleteUser() {
		JsonNode json = request().body().asJson();
		String email = json.findPath("email").textValue();
		if (email == null) {
			ObjectNode response = Json.newObject();
			response.put("Error", "Invalid Json format.");
			return ok(response);
		} else {
			if (User.find.byId(email) == null) {
				ObjectNode response = Json.newObject();
				response.put("Error", "No such user exists.");
				return ok(response);
			} else {
				if (User.find.byId(email).getAdminStatus()) {
					User.deleteUser(email);
					ObjectNode response = Json.newObject();
					response.put("Success", "User deleted succesfully.");
					return ok(response);
				} else {
					ObjectNode response = Json.newObject();
					response.put("Error", "Unauthorized action.");
					return ok(response);
				}
			}
		}
	}

	public Result chargerCloud(String apikey) {
		if (apikey != null) {
			Company comp = Company.findByApiKey(apikey);
			if (comp != null) {
				Payment payment=comp.payment;
				JsonNode json = request().body().asJson();
				ObjectNode response = Json.newObject();
				if (payment.method == PaymentMethods.Free) {
					LocalDate today = new LocalDate();
					LocalDate limitDate=payment.startDate.plusMonths(1);
					if(limitDate.isBefore(today)){
					if(comp.getCallAmount()<comp.payment.callLimit){
						comp.apiCall();
						comp.save();
						response.put("Success", "Request complete");
						return ok(response);
					}
					else{
						response.put("Failure", "You have reached your usage limit");
						return ok(response);
					}
					}
					else{
						comp.resetCall();
						comp.apiCall();
						comp.save();
						response.put("Success", "Request complete");
						return ok(response);
					}
				}
				else if (payment.method == PaymentMethods.Billing) {
					Billing bill =payment.billing;
					LocalDate today = new LocalDate();
					if(today.isAfter(bill.endDate)){
						Invoice inv= comp.getCurrentInvoice();
						if(inv!=null){
							if(inv.isPaid() || inv.dueDate.isAfter(today)){
								response.put("Success", "Request complete");
								return ok(response);
							}
							else{
								response.put("Error", "You have unpaid invoices.");
								return ok(response);	
							}
						}
						else{
							Invoice newInv = new Invoice();
							newInv.save();
							comp.InvoiceList.add(newInv);
							comp.save();
							response.put("Success", "Request complete");
							return ok(response);
						}
					}
					else{
						response.put("Success", "Request complete");
						return ok(response);
					}
				}
				else  {
					PrePaid prep= payment.prepaid;
					LocalDate today = new LocalDate();
					if(today.isAfter(prep.endDate)){
						response.put("Error", "Your subscription has ended.");
						return ok(response);
					}
					else{
						response.put("Success", "Request complete");
						return ok(response);
					}
				}
			} else {
				ObjectNode response = Json.newObject();
				response.put("Error", "Invalid api key.");
				return ok(response);
			}
		} else {
			ObjectNode response = Json.newObject();
			response.put("Error", "No provided api key.");
			return ok(response);
		}
	}

	public Result validate(String urlsafe) {
		List<User> userlist = User.find.all();
		String hashed = urlsafe.replaceAll("-", "/");
		String name = "";
		boolean verification = false;
		for (int i = 0; i < userlist.size(); i++) {
			User user = userlist.get(i);
			verification = Password.checkPassword(user.email.toString(), hashed);
			if (verification) {
				if (user.getRegistered() == true) {
					return ok(views.html.registered.render());
				} else {
					name = user.name.toString();
					User us = User.find.byId(user.email);
					us.register();
					us.save();
					break;
				}

			}
		}
		if (verification) {
			return redirect("https://chargercloud.herokuapps.com");
		} else {
			return redirect(routes.HomeController.error());
		}

	}

	public Result error() {
		return ok(error.render());
	}

	public Result valComplete(String username) {
		return ok(validate.render(username));
	}

	public Result main() {
		return ok(main.render(formFactory.form(User.class)));
	}

	@Security.Authenticated(Secured.class)
	public Result logout() {
		User user = User.find.byId(request().username());
		User.logout(user);
		session().clear();
		response().discardCookie("Authorization");
		user.deleteAuthToken();
		ObjectNode response = Json.newObject();
		response.put("Success", "User successfully logged out.");
		return ok(response);

	}

	public Result login() {
		return ok(login.render());
	}

	public Result authenticate() {
		JsonNode json = request().body().asJson();
		if (json.findPath("email").textValue() == null || json.findPath("password").textValue() == null) {
			ObjectNode response = Json.newObject();
			response.put("Error", "Missing parameters in the Json.");
			return ok(response);
		}
		if (User.find.byId(json.findPath("email").textValue()) == null) {
			ObjectNode response = Json.newObject();
			response.put("Error", "No such user exists.");
			return ok(response);
		} else {
			User user = User.find.byId(json.findPath("email").textValue());
			if (user.password == null || user.email == null) {
				ObjectNode response = Json.newObject();
				response.put("Error", "Invalid Json format!");
				return ok(response);
			} else {
				if (user.getRegistered() == false) {
					ObjectNode response = Json.newObject();
					response.put("Error", "Please verify your email to login.");
					return ok(response);
				} else {
					if (User.authenticate(json.findPath("email").textValue(),
							json.findPath("password").textValue()) == null) {
						ObjectNode response = Json.newObject();
						response.put("Error", "Invalid Username or Password");
						return ok(response);
					} else {
						User.login(User.find.byId(json.findPath("email").textValue()));
						session().clear();
						session("email", json.findPath("email").textValue());
						String authToken = user.createToken();
						ObjectNode authTokenJson = Json.newObject();
						authTokenJson.put("Authorization", authToken);
						// response().setCookie(Http.Cookie.builder("Authorization",
						// authToken).withSecure(ctx().request().secure()).build());
						return ok(authTokenJson);
					}

				}
			}
			/*
			 * if (loginForm.hasErrors() || loginForm == null) { return
			 * badRequest(login.render(loginForm)); } else { User user =
			 * User.find.byId(loginForm.get().email); if (user.getRegistered()
			 * == false) { flash("failure", "Please verify your email"); return
			 * badRequest(login.render(loginForm)); } else {
			 * 
			 * User.login(User.find.byId(loginForm.get().email));
			 * session().clear(); session("email", loginForm.get().email);
			 * return redirect(routes.HomeController.home()); String authToken =
			 * user.createToken(); ObjectNode authTokenJson = Json.newObject();
			 * authTokenJson.put("Authorization", authToken);
			 * response().setCookie(Http.Cookie.builder("Authorization",
			 * authToken).withSecure(ctx().request().secure()).build()); return
			 * ok(authTokenJson);
			 * 
			 * }
			 */

		}
	}

	// TODO utiliy class
	public boolean passwordChecker(String pass) {
		if (pass.length() < 6) {
			flash("failure", "Your password must contain atleast 6 characters!");
			return true;
		} else {
			boolean upper = false;
			boolean lower = false;
			boolean number = false;
			for (char c : pass.toCharArray()) {
				if (Character.isUpperCase(c)) {
					upper = true;
				} else if (Character.isLowerCase(c)) {
					lower = true;
				} else if (Character.isDigit(c)) {
					number = true;
				}
			}
			if (!upper) {
				flash("failure", "Your password must contain atleast one uppercase letter!");
				return true;
			} else if (!lower) {
				flash("failure", "Your password must contain atleast one lowercase letter!");
				return true;
			} else if (!number) {
				flash("failure", "Your password must contain atleast one number!");
				return true;
			} else {
				return false;
			}
		}
	}
	/*
	 * public static class Login { public String email; public String password;
	 * 
	 * 
	 * public String validate() { if (User.authenticate(email, password) ==
	 * null) { return "Invalid user or password"; } return null; }
	 * 
	 * }
	 */
}
