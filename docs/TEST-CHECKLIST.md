# Manual test checklist

Run this before a demo. Use two browser windows, one normal and one private, so you can be two users at once.

## Setup

- [ ] `schema.sql` runs without errors.
- [ ] The app starts, and http://localhost:8080/stemroom/ opens the login page.

## Accounts

- [ ] Log in as admin@stemroom.local with admin123. The admin menu appears.
- [ ] A wrong password shows "Email or password is wrong."
- [ ] Register a new musician, then log out.
- [ ] Turn sign-ups off in settings. The register page says sign-ups are closed.
- [ ] As a musician, open /admin/users. The page says "Not allowed."
- [ ] Logged out, open /projects. You are sent to the login page.

## Projects

- [ ] Create a project with only a title.
- [ ] An empty title is refused.
- [ ] A deadline that is not a date is refused.
- [ ] The owner adds mira@stemroom.local by email.
- [ ] Adding the same person again shows "already on this project."
- [ ] Mira sees the project in her list and can change its status.
- [ ] A musician who is not on a project, opening its detail page, gets "Not found."
- [ ] Set the project limit to 1 in settings. A second project is refused.

## Portfolio

- [ ] Add a song with an mp3 file. It shows as pending.
- [ ] Upload a .txt file. It is refused.
- [ ] Delete a song. It disappears, and its file is removed from the stemroom-uploads folder.

## Admin

- [ ] Approve a pending song. The status changes to approved.
- [ ] Reject a song. The status changes to rejected.
- [ ] Change a user's role. Their menu changes on the next page load.
- [ ] The admin cannot change or delete their own account.
- [ ] Delete a test account. Its projects are gone too.

## Layout

- [ ] At 360px wide, the top bar wraps, forms fill the width, and tables scroll inside their own box.
- [ ] At 1280px wide, the content is centred and no wider than 900px.
- [ ] Tab through the login form. The focus outline is visible on each field and button.
