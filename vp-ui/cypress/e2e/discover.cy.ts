describe('Discover page', () => {
  it('visit discover', () => {
    cy.visit('/discover')

    cy.get(".discover__header").should("be.visible")
    cy.get(".discover__header h1").should("have.text", "VisualProductivity")
    cy.get(".discover__header button").click()

    cy.get('.mdc-list-item__content span').contains("Se connecter").click()

    cy.url().should("include", "/login")

    cy.visit('/discover')
    cy.get(".discover__header button").click()
    cy.get('.mdc-list-item__content span').contains("S'inscrire").click()
    cy.url().should("include", "/register")
  })
})
