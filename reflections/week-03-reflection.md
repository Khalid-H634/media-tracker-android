# Week {{03}} Reflection

**Name:**
**Date:**

---
Khalid Hassan 06-04-2026
## Commits This Week

<!-- Paste a link to your commits for this week. The easiest way: go to your repo on GitHub,
     click "commits", and copy the URL after filtering by your name or branch. -->

**Link:**

---
 Khalid Hassan
https://github.com/Khalid-H634/media-tracker-android/pull/2/changes/ee9e1200fe1f19729edbb0a56145578cd346ec41

## Code Review

<!-- Every week you leave a review on a pod mate's pull request. Fill in both parts below.
     Part 1 is the link — I will verify the review exists on GitHub.
     Part 2 is your written assessment — what you actually looked at and what you found. -->

**Reviewed:** *(pod mate's name)*
**Link to my review:**
Hunter Bounty and Danny Kins review.
https://github.com/DannyKin/media-tracker-android/pull/3/changes
https://github.com/Hunterbounty11/media-tracker-android/pull/3


### What I Looked At

<!-- Walk through the code you reviewed. What was the PR trying to do? Which files or
     functions did you focus on? -->


What I focused is checking their registerScreen on what they have added, As well as their
API Service setup and UserRepository.


### What I Noticed

<!-- Be specific. Did you spot a potential bug? A pattern that could cause problems? Something
     done well that you want to call out? "I looked at the ViewModel and everything seemed fine"
     is not specific enough. Name the thing you noticed and explain why it matters. -->
I Notice that some errors from all of us more specifically from under registerScreen where we all have

Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
Text(Collapse commentComment on line L18Khalid-H634 commented on Jun 4, 2026 Khalid-H634on Jun 4, 2026More actionsNice job, I also had this issue.ReactWrite a replyResolve comment
text = stringResource(edu.metrostate.ics342.mediatracker.R.string.register_not_implemented),
style = MaterialTheme.typography.bodyLarge

which was show an red error when trying to run it. 
### Comments I Left

<!-- Briefly summarize the comments you left on the PR. If you left a positive comment,
     say what it was. If you left a suggestion, say what you suggested and why. -->

---
I left A few comments under Danny Kings and Hunters post, about the amount of imports weve added under
RegisteredScreen. One of the things i notice is that alot of the texts except for (Smart display) image 
shows up a red error without it. 
## One Thing I Understood More Deeply

<!-- Be specific. Don't write "I learned about ViewModels." Write what specifically clicked —
     what was confusing before, what made it make sense, and how you'd explain it to someone else.
     There are no wrong answers here. -->

---
I feel like I have an understanding on the relationship between the API Interface and the
Repository file. For the RegisterScreen file when filling the information in it just lets you view 
the UserInterface. it does not store or collects inputs let alone create an account.
the Api service felt more like A endpoint that sends users to backend.

## One Thing I'm Still Confused About

<!-- Be honest. This is the most useful part of the reflection for me — it tells me where to
     spend more time in class. You will not lose points for being confused. -->

---
Early I got confused on the CreateUserRequest error from API Service fault. As well as importing an image
file from google fonts and store it in the Android studios. What I got confused on is that is there an
option to just copy the code directly from Google fonts and i was wondering why not just paste it into the file/class instead of
importing it directly
## Anything Else *(optional)*

<!-- Did you help a pod mate work through something? Did you discover something cool or frustrating?
     Did something from a previous week finally click? This is a good place to put it. -->

---
One of my Podmates undertsood what to do after he created another class file based on userCreatedrequest.
Im still figuring out as well. 
## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
