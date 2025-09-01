<script setup lang="ts">
const config = useRuntimeConfig();
if (import.meta.client && !config.public.isDev) {
  // ==================== CORE PROTECTION MECHANISMS ====================

  let timer;
  // Main protection interval - multiple detection methods running at different intervals
  timer = setInterval(() => {
    executePrimaryProtections();
  }, 1000);

  // // Secondary protection with randomized intervals to avoid pattern detection
  // setInterval(
  //   () => {
  //     executeSecondaryProtections();
  //   },
  //   Math.random() * 50 + 50
  // );

  // // Tertiary protection with longer intervals for heavier checks
  // setInterval(() => {
  //   executeTertiaryProtections();
  // }, 1000);

  // ==================== PROTECTION FUNCTIONS ====================

  function executePrimaryProtections() {
    /**
     * Immediate detection checks that run very frequently
     */

    // Window size difference detection (DevTools open)
    if (
      Math.abs(outerWidth - innerWidth) > 99 ||
      Math.abs(outerHeight - innerHeight) > 125
    ) {
      triggerProtection();
    }

    // Time manipulation detection
    if (new Date().getTime() - performance.now() > 9) {
      triggerProtection();
    }

    // Function toString tampering detection
    if (Function.prototype.toString.toString().indexOf("native") === -1) {
      triggerProtection();
    }
  }

  function executeSecondaryProtections() {
    /**
     * Medium-weight checks that run less frequently
     */

    // Debugger function detection
    try {
      const debuggerTest = new Function("debugger;");
      if (debuggerTest.toString().length !== 15) {
        // Normal: "function anonymous() { debugger; }"
        triggerProtection();
      }
    } catch (e) {
      triggerProtection();
    }

    // Performance monitoring detection
    const start = performance.now();
    (() => {}).toString();
    const duration = performance.now() - start;
    if (duration > 1) {
      // Breakpoints slow down execution
      triggerProtection();
    }

    // DevTools user agent detection (partial matching)
    if (
      navigator.userAgent.match(/Firefox|Chrome|Safari|Edge|DevTools|Inspect/gi)
    ) {
      triggerProtection();
    }
  }

  function executeTertiaryProtections() {
    /**
     * Heavy checks that run less frequently
     */

    // Function redefinition detection
    const originalToString = Function.prototype.toString;
    Function.prototype.toString = function () {
      if (this === originalToString) {
        triggerProtection();
      }
      return originalToString.call(this);
    };

    // Console method tampering detection
    // ["log", "error", "warn", "info", "debug", "trace"].forEach((method) => {
    //   if (console[method].toString().indexOf("native") === -1) {
    //     triggerProtection();
    //   }
    // });

    // Eval length tampering detection
    if (eval.length !== 0) {
      // Normal eval has length 0
      triggerProtection();
    }
  }

  // ==================== PROTECTION TRIGGERS ====================

  function triggerProtection() {
    // Multiple protection layers triggered simultaneously
    // 1. Infinite debugger loop (primary)
    alert("f");
    infiniteDebuggerLoop();
    // 2. Console flooding (secondary)
    //floodConsole();
    // 3. Memory consumption (tertiary)
    //consumeMemory();
    // 4. Redirect or close window (nuclear option)
    // window.close(); // Uncomment for extreme measures
  }

  function infiniteDebuggerLoop() {
    // Multiple debugger statements with random code to prevent pattern matching
    const debuggers = [
      () => {
        for (;;) debugger;
      },
      () => {
        while (true) {
          debugger;
        }
      },
      () => {
        debugger;
        debugger;
        debugger;
        infiniteDebuggerLoop();
      },
    ];

    const randomDebugger =
      debuggers[Math.floor(Math.random() * debuggers.length)];
    randomDebugger();
  }

  function floodConsole() {
    // Flood console with random data to obscure debugging
    const floodMessages = [
      "Debugging attempt detected",
      "Security violation",
      "Tampering detected",
      "Unauthorized access",
      "Protection triggered",
      Math.random().toString(36).slice(2),
      new Date().toISOString(),
      performance.now().toString(),
    ];

    setInterval(() => {
      const randomMessage =
        floodMessages[Math.floor(Math.random() * floodMessages.length)];
      try {
        console.log(randomMessage);
        console.error(randomMessage);
        console.warn(randomMessage);
      } catch (e) {
        // Ignore errors from console being blocked
      }
    }, 50);
  }

  function consumeMemory() {
    // Gradually consume memory to slow down debugging tools
    const memoryHog = [];
    setInterval(() => {
      memoryHog.push(new Array(1000).fill(Math.random()));
    }, 100);
  }

  // ==================== INITIAL PROTECTION SETUP ====================

  // Protect console methods with multiple layers
  if (!config.public.isDev)
    ["log", "error", "warn", "info", "debug", "trace", "table", "dir"].forEach(
      (method) => {
        Object.defineProperty(console, method, {
          value: function () {
            triggerProtection();
            return undefined;
          },
          writable: false,
          configurable: false,
        });
      }
    );

  // Create deceptive objects that trigger protection when interacted with
  const deceptiveObjects = [
    {
      toString: () => {
        triggerProtection();
        return "";
      },
    },
    {
      valueOf: () => {
        triggerProtection();
        return 0;
      },
    },
    {
      then: (resolve) => {
        triggerProtection();
        resolve();
      },
    },
  ];

  deceptiveObjects.forEach((obj) => {
    try {
      console.log(obj);
    } catch (e) {
      // Ignore errors
    }
  });

  // Protect against property access on window object
  const protectedProperties = ["a", "b", "debug", "inspect", "devtools"];
  protectedProperties.forEach((prop) => {
    Object.defineProperty(window, prop, {
      get: () => {
        triggerProtection();
        return undefined;
      },
      set: () => triggerProtection(),
      configurable: false,
      enumerable: false,
    });
  });

  // ==================== ADVANCED DETECTION TECHNIQUES ====================

  // Detect breakpoints by comparing expected vs actual execution time
  function detectBreakpoints() {
    const testCode = `const start = performance.now();
    for(let i = 0; i < 1000; i++) { Math.sqrt(i); }
    return performance.now() - start;`;

    const normalTime = new Function(testCode)();
    const currentTime = new Function(testCode)();

    if (currentTime > normalTime * 5) {
      // Significant slowdown
      triggerProtection();
    }
  }

  // Detect if code is being beautified/minified
  function detectCodeModification() {
    const originalFunctionLength = executePrimaryProtections.toString().length;
    const currentFunctionLength = detectCodeModification.toString().length;

    // If function lengths don't match expectations
    if (Math.abs(currentFunctionLength - originalFunctionLength) > 100) {
      triggerProtection();
    }
  }

  // ==================== FINAL SETUP ====================

  // Run initial detection checks
  executePrimaryProtections();
  // executeSecondaryProtections();
  // executeTertiaryProtections();

  // Start periodic advanced detection
  // setInterval(detectBreakpoints, 5000);
  // setInterval(detectCodeModification, 10000);

  // Final nuclear option - if all else fails
  window.addEventListener("devtoolschange", (e) => {
    if (e.detail.open) {
      triggerProtection();
    }
  });

  // Disable right-click
  document.addEventListener("contextmenu", (e) => e.preventDefault());

  // Disable F12, Ctrl+Shift+I, Ctrl+U, Ctrl+S
  document.addEventListener("keydown", (e) => {
    if (
      e.key === "F12" ||
      (e.ctrlKey && e.shiftKey && (e.key === "I" || e.key === "J")) ||
      (e.ctrlKey && (e.key === "U" || e.key === "S"))
    ) {
      e.preventDefault();
    }
  });
}
import { useUserStore } from "./stores/user";
import type { User } from "./types/User";
const userStore = useUserStore();
const { gtag } = useGtag();
const { t } = useI18n();
const headers = useRequestHeaders(["cookie"]);

const username = computed(() => {
  return (
    (userStore.isUser && userStore.user.username) ||
    t("auth.username_guest") ||
    "Guest"
  );
});

function hasRole(role: string) {
  return !!(
    userStore.user &&
    Array.isArray(userStore.user.roles) &&
    userStore.user.roles.includes(role)
  );
}

const { data: user } = await useAsyncData<User>("user", async () => {
  try {
    return await $fetch<User>(
      import.meta.server ? "http://localhost:3000/api/me" : "/api/me",
      {
        credentials: "include",
        headers,
      }
    );
  } catch {
    return {
      username: "Guest",
      roles: [],
    };
  }
});

userStore.init(user.value!);
console.log("Client user initialized!");
console.table(user.value);

watch(
  () => userStore.loaded,
  () => {
    gtag("event", "screen_view", {
      app_name: "udevkit",
      screen_name: "Home",
    });
    console.table(userStore.$state);
  }
);
const runtimeConfig = useRuntimeConfig();
</script>

<template>
  <MobileHeader />
  <main>
    <NuxtPage />
  </main>
</template>

<style scoped>
main {
  margin-inline: auto;
  max-width: 768px;
  padding-block: 1rem 0;
  padding-inline: 1rem;
  min-height: calc(100dvh);
  box-shadow: 0 2px 16px rgba(35, 41, 70, 0.07);
  border-left: 1px solid #cccccc;
  border-right: 1px solid #cccccc;
}
</style>

<style>
*,
*::before,
*::after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}
textarea {
  resize: none;
}
html {
  background-color: #ffffff;
  /* This prevent horizontal scrolling on iOS */
  overflow-x: hidden;

  background: #eeeeee;
}
h1 {
  margin-top: 0;
}
</style>
