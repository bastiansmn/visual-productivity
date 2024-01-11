describe('Auth flow', function () {
  it('should try to log in', function () {
    cy.visit('/login');

    cy.get("a[routerLink='/register']").click();
    cy.url().should('include', '/register');

    cy.get("input[name='name']").type("toto");
    cy.get("input[name='name']").should('have.class', 'ng-valid');
    cy.get("input[name='name']").clear();
    cy.get("input[name='name']").should('have.class', 'ng-invalid');
    cy.get("input[name='name'] + small.errorMessage").should('contain.text', 'Précisez votre prénom');
    cy.get("input[name='name']").type("toto");

    cy.get("input[name='lastname']").type("toto");
    cy.get("input[name='lastname']").should('have.class', 'ng-valid');
    cy.get("input[name='lastname']").clear();
    cy.get("input[name='lastname']").should('have.class', 'ng-invalid');
    cy.get("input[name='lastname'] + small.errorMessage").should('contain.text', 'Précisez votre nom');
    cy.get("input[name='lastname']").type("toto");

    testEmail();
    // Typing mail that should already exist
    cy.get("input[name='email']").type(Cypress.env("email"));
    cy.get("input[name='email']").should('have.class', 'ng-valid');

    cy.get("input[name='password']").type("toto");
    cy.get("input[name='password']").should('have.class', 'ng-invalid');
    cy.get("input[name='password'] + small.errorMessage").should('contain.text', 'Votre mot de passe est trop court');
    cy.get("input[name='password']").clear();
    cy.get("input[name='password']").should('have.class', 'ng-invalid');
    cy.get("input[name='password'] + small.errorMessage").should('contain.text', 'Veuillez saisir un mot de passe');
    cy.get("input[name='password']").type(Cypress.env("password"));
    cy.get("input[name='password']").should('have.class', 'ng-valid');

    cy.get("input[name='passwordConfirm']").type("toto");
    cy.get("input[name='passwordConfirm']").should('have.class', 'ng-invalid');
    cy.get("input[name='password']").should('have.class', 'ng-invalid');
    cy.get("input[name='passwordConfirm'] + small.errorMessage").should('contain.text', 'Les mots de passe ne correspondent pas');
    cy.get("input[name='password'] + small.errorMessage").should('contain.text', 'Les mots de passe ne correspondent pas');
    cy.get("input[name='passwordConfirm']").clear();
    cy.get("input[name='passwordConfirm']").should('have.class', 'ng-invalid');
    cy.get("input[name='passwordConfirm'] + small.errorMessage").should('contain.text', 'Veuillez confirmez votre mot de passe');
    cy.get("input[name='passwordConfirm']").type(Cypress.env("password"));
    cy.get("input[name='passwordConfirm']").should('have.class', 'ng-valid');

    cy.get('.vp__check > label').click();

    // TODO: use fixture to mock the response
    cy.fixture("registration/failed").then((register) => {
      expect(register).to.have.property('devMessage');
      expect(register).to.have.property('message');
      expect(register).to.have.property('httpStatus');
      expect(register.devMessage).to.contain("USER_0001");
      expect(register.httpStatus).to.equal(400);
      expect(register.message).to.contain("Un utilisateur avec cet email existe déjà");
    });

    cy.visit('/login');

    testEmail();

    cy.get("input[name='email']").type("wrong.email@gmail.com")
    cy.get("input[name='email']").should('have.class', 'ng-valid');


    cy.get("input[name='password']").type("toto");
    cy.get("input[name='password']").clear();
    cy.get("input[name='password']").should('have.class', 'ng-invalid');
    cy.get("input[name='password'] + small.errorMessage").should('contain.text', 'Veuillez saisir un mot de passe');
    cy.get("input[name='password']").type("wrong.password");
    cy.get("input[name='password']").should('have.class', 'ng-valid');

    cy.get('.vp__check > label').click();

    cy.intercept('POST', '/api/v1/login').as('login');
    cy.get('.vp__button').click();
    cy.wait('@login').then((interception) => {
       if (!interception.response) return;
       expect(interception.response.statusCode).to.equal(403);
    });

    cy.get('.vp__check > label').click();

    cy.get("input[name='email']").clear();
    cy.get("input[name='email']").type(Cypress.env("email"));
    cy.get("input[name='password']").clear();
    cy.get("input[name='password']").type(Cypress.env("password"));

    cy.intercept('POST', '/api/v1/login').as('login');
    cy.get('.vp__button').click();
    cy.wait('@login').then((interception) => {
       if (!interception.response) return;
       expect(interception.response.statusCode).to.equal(200);
    });

    cy.url().should('include', '/app/dashboard');
    cy.getCookie('accessToken').should('exist');
  });
});

function testEmail() {
  cy.get("input[name='email']").type("toto");
  cy.get("input[name='email']").should('have.class', 'ng-invalid');
  cy.get("input[name='email'] + small.errorMessage").should('contain.text', 'Email invalide');
  cy.get("input[name='email']").clear();
  cy.get("input[name='email']").should('have.class', 'ng-invalid');
  cy.get("input[name='email'] + small.errorMessage").should('contain.text', 'Veuillez saisir un email');
}
