package controllers;

import java.util.List;

import javax.inject.Inject;

import validator.Validador;
import models.User;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.data.format.Formatters;
import play.i18n.MessagesApi;
import play.mvc.*;
import views.html.*;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import utility.Password;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class HomeController extends Controller {

	/**
	 * An action that renders an HTML page with a welcome message. The
	 * configuration in the <code>routes</code> file means that this method will
	 * be called when the application receives a <code>GET</code> request with a
	 * path of <code>/</code>.
	 */
	@Inject
	MailerClient mailerClient;

	MessagesApi msgApi = new MessagesApi(null);
	@Inject
	FormFactory formFactory = new FormFactory(msgApi, new Formatters(msgApi),
			new Validador());

	public void sendEmail(String emailadress) {
		String cid = "1234";
		Email email = new Email();
		email.setSubject("Account Verification");
		email.setFrom("from@email.com");
		email.addTo(emailadress);
		email.setBodyText("Click to this link to verify your account:https://www.google.com.tr/");
		mailerClient.send(email);
	}

	public Result newUser() {
		Form<User> userForm = formFactory.form(User.class).bindFromRequest();
		if (userForm.hasErrors()) {
			return badRequest(views.html.main.render(userForm));
		} else {
			if (User.find.byId(userForm.get().email) == null) {
				String pass = userForm.get().password;
				if (passwordChecker(pass)) {
					return badRequest(views.html.main.render(userForm));
				} else {
					User.create(userForm.get());
					flash("success",
							"You've have succesfully created an account");
					// sendEmail(userForm.get().email);
					return ok(views.html.login.render(formFactory
							.form(Login.class)));
				}
			} else {
				flash("failure", "Account already exists");
				return badRequest(views.html.main.render(userForm));
			}
		}
	}

	@Security.Authenticated(Secured.class)
	public Result home() {
		return ok(home.render(User.find.all(),
				User.find.byId(request().username())));
	}

	public Result main() {
		return ok(main.render(formFactory.form(User.class)));
	}

	public Result logout() {
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.HomeController.login());
	}

	public Result login() {
		return ok(login.render(formFactory.form(Login.class)));
	}

	public Result authenticate() {
		Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(login.render(loginForm));
		} else {
			session().clear();
			session("email", loginForm.get().email);
			return redirect(routes.HomeController.home());
		}
	}

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
				flash("failure",
						"Your password must contain atleast one uppercase letter!");
				return true;
			} else if (!lower) {
				flash("failure",
						"Your password must contain atleast one lowercase letter!");
				return true;
			} else if (!number) {
				flash("failure",
						"Your password must contain atleast one number!");
				return true;
			} else {
				return false;
			}
		}
	}

	public static class Login {

		public String email;
		public String password;

		public String validate() {
			if (User.authenticate(email, password) == null) {
				return "Invalid user or password";
			}
			return null;
		}

	}
}
