Main Objective
Write a thread-safe banking system that can handle thousands of simultaneous (multithreaded) money transfers between different accounts without losing data consistency or causing system freezes.
🛠️ Functional Requirements
1. Data Model:
   • Create an Account class containing a unique account identifier (String id) and the current balance (BigDecimal balance).
2. Transfer Operation:
   • Create a TransferTask class or method that accepts a source account, a destination account, and the transfer amount.
   • The transfer must deduct the amount from Account A and add it to Account B.
3. Concurrency:
   • Generate a base of 100 accounts, each starting with a balance of $10,000.
   • Spin up a thread pool (ExecutorService) and execute 100,000 random transfers between these accounts simultaneously.
4. Verification (System Invariant):
   • The total sum of money across all accounts before the transfers start must be exactly equal to the total sum after all threads finish their work (not a single cent can disappear or be created out of nowhere).

Core Challenges (What you need to watch out for)
• Race Conditions: Two or more threads must not modify the balance of the same account at the same exact time without proper synchronization, otherwise data corruption will occur.
• Deadlock: If Thread 1 transfers money from Account A to Account B (locking A and waiting for B), while Thread 2 simultaneously transfers from Account B to Account A (locking B and waiting for A), the application will freeze forever. You must prevent this.
• Insufficient Funds: A transfer must not proceed if the source account does not have enough money to cover the transaction amount.