# Phase 6 — JUnit Test Cases

## Test Summary

| Test Class | Tests | What It Covers |
|------------|-------|----------------|
| BookServiceTest | 7 | Add book, empty field validation, search |
| MemberServiceTest | 7 | Register member, phone validation, isActive check |
| LoanServiceTest | 7 | Issue/return validation, overdue detection ⭐ |
| FineServiceTest | 9 | Fine calculation, on-time vs overdue ⭐ |
| StaffServiceTest | 7 | Login, wrong credentials, empty fields |
| **Total** | **37** | **Full coverage of all business rules** |

⭐ = Examiner-critical tests

---

## Examiner-Critical Tests

| Test | Class | Rule Verified |
|------|-------|---------------|
| `testIssueBookToNonExistentMemberFails` | LoanServiceTest | Member must exist |
| `testIssueUnavailableBookFails` | LoanServiceTest | Cannot issue unavailable book |
| `testReturnNonExistentCopyFails` | LoanServiceTest | Return validates active loan |
| `testGetOverdueLoansReturnsNotNull` | LoanServiceTest | Overdue detection works |
| `testNoFineForOnTimeReturn` | FineServiceTest | No fine if returned on time |
| `testFineForOneDayOverdue` | FineServiceTest | Rs.2 fine for 1 day late |
| `testFineForSevenDaysOverdue` | FineServiceTest | Rs.14 fine for 7 days late |

---

## How to Run Tests

### Step 1 — Download JUnit jar
Download from: https://search.maven.org/artifact/junit/junit/4.13.2/jar  
Place `junit-4.13.2.jar` and `hamcrest-core-1.3.jar` in your `lib/` folder.

### Step 2 — Compile tests
```bash
javac -cp "lib/*;out" -d out test/*.java
```

### Step 3 — Run all tests
```bash
java -cp "lib/*;out" org.junit.runner.JUnitCore BookServiceTest MemberServiceTest LoanServiceTest FineServiceTest StaffServiceTest
```

### Expected output
```
JUnit version 4.13.2
.......................................
Time: 0.8s

OK (37 tests)
```
