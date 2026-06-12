# Week 04 Reflection

**Name:** Khalid Hassan
**Date:** 06-11-2026

---

## Commits This Week



**Link:**
https://github.com/Khalid-H634/media-tracker-android/pull/3/commits


## Code Review



**Reviewed:** *(pod mate's name)*
**Link to my review:**

 Hunter Bounty
https://github.com/Hunterbounty11/media-tracker-android/pull/4/changes/18274ce1a66847379882514ae9b79060635f84e9 


Danny King
https://github.com/DannyKin/media-tracker-android/pull/4/changes/cc8ff3d6ecee0774144bc202519b3d62f6f1d805#r3400376865
### What I Looked At

I looked at both of their RegisterScreen files and how they set up their form state.

### What I Noticed

Hunter had all his form state in the ViewModel using collectAsState() which is organized. Danny had the right imports ready for his composable

### Comments I Left
I let Danny know about how his imports may not have been synced well. and Hunter I just told him about his viewmodel. 



## One Thing I Understood More Deeply
I think I get ScrollState. When the keyboard pops up, the screen refreshes and the scroll spot goes back to the top. 
ScrollState saves that spot so you can scroll down and tap the Register button.



## One Thing I'm Still Confused About


I'm confused about how  RegisterScreen sends the email, password, and display name to the ViewModel.
Also my app for some reason crashes whenever I try to click register on the emulator. I also made a separate CreateUserFile under model folder to figure out where the data goes.
    


## Anything Else *(optional)*





## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
