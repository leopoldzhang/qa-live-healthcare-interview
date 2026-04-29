# 标准编码风格文档

## 概述
本文档定义了 qa-live-healthcare-interview 工作区的编码标准和风格指南。一致的编码风格可以提高代码的可读性、可维护性和协作效率。

## 通用原则

### 1. 可读性优先
- 代码应易于阅读和理解
- 为变量、函数、类使用有意义的名称
- 编写自文档化代码，意图清晰

### 2. 一致性
- 在整个代码库中遵循相同的模式
- 遵循语言和框架的既定约定
- 保持团队成员之间的一致性

### 3. 可维护性
- 编写易于修改和扩展的代码
- 保持函数和类的单一职责
- 避免不必要的复杂性

## 语言特定指南

### TypeScript/Vue 3

#### 命名约定
```typescript
// 变量和函数 - camelCase
const userName = 'John';
const activeDoctors = ref<Doctor[]>([]);
function calculateTotal() { }

// 类、接口、类型 - PascalCase
class UserService { }
interface Doctor { }
type PatientData = { id: string; name: string; }

// 常量 - UPPER_SNAKE_CASE
const MAX_RETRY_COUNT = 3;
const API_BASE_URL = 'http://localhost:8080';

// 组件 - PascalCase.vue
// AppHeader.vue, DoctorLogin.vue

// 组合式函数 - camelCase (use 前缀)
// useDoctorList.ts, useAuth.ts
```

#### 代码格式
```typescript
// 使用 2 空格缩进
function example() {
  if (condition) {
    // ...
  }
}

// 使用分号
const name = 'John';
const data = { id: 1 };

// 最大行长度: 100 字符
// 长行换行以提高可读性

// 字符串使用单引号（除非需要插值）
const message = 'Hello';
const template = `Hello ${name}`;

// 对象/数组末尾逗号
const config = {
  apiUrl: '...',
  timeout: 5000,
};
```

#### 类型注解
```typescript
// 始终指定返回类型
function add(a: number, b: number): number {
  return a + b;
}

// 使用显式类型而非 'any'
// 错误
function process(data: any) { }

// 正确
function process(data: Doctor) { }

// 使用 interface 定义对象形状
interface Doctor {
  id: string;
  name: string;
  department: string;
  title: string;
}

// 使用 type 定义联合类型或复杂类型
type UserRole = 'patient' | 'doctor' | 'admin';
type ApiResponse<T> = {
  code: number;
  data: T;
  message: string;
};
```

#### Vue 3 组件风格
```vue
<!-- 使用 <script setup> 语法 -->
<script setup lang="ts">
import { ref, computed } from 'vue';
import type { Doctor } from '../store';

// 使用 camelCase 命名变量
const activeDoctors = ref<Doctor[]>([]);
const loading = ref(false);

// 使用 async/await 处理异步
const loadDoctors = async () => {
  try {
    loading.value = true;
    activeDoctors.value = await store.getActiveDoctors();
  } catch (error) {
    console.error('Failed to load doctors:', error);
  } finally {
    loading.value = false;
  }
};

// 使用 computed 计算属性
const doctorCount = computed(() => activeDoctors.value.length);
</script>

<!-- 模板中使用 kebab-case 引用组件 -->
<template>
  <div class="doctor-list">
    <app-header :title="title" />
    <doctor-card
      v-for="doctor in activeDoctors"
      :key="doctor.id"
      :doctor="doctor"
    />
  </div>
</template>

<!-- 样式使用 scoped 限制作用域 -->
<style scoped>
.doctor-list {
  padding: 24px;
}
</style>
```

### Java (Spring Boot)

#### 命名约定
```java
// 类和接口 - PascalCase
public class UserService { }
public interface DoctorRepository { }

// 方法和变量 - camelCase
public void calculateTotal() { }
private String userName;

// 常量 - UPPER_SNAKE_CASE
public static final int MAX_RETRY_COUNT = 3;
private static final String API_BASE_URL = "http://example.com";

// 包名 - 全小写
package com.leansofx.qaserviceuser.controller;

// 注解 - PascalCase
@RestController, @Service, @Autowired
```

#### 代码格式
```java
// 使用 2 空格缩进（项目实际风格）
@RestController
public class AuthController {
  @Autowired
  private PatientService patientService;
  
  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> login(
      @RequestBody LoginRequest request) {
    // ...
  }
}

// 开括号在同一行
public void example() {
  // ...
}

// 一行一条语句
// 错误
int a = 1; int b = 2;

// 正确
int a = 1;
int b = 2;
```

#### 注解和修饰符
```java
// 标准修饰符顺序
public static final String CONSTANT = "value";

// 使用 @Override 注解
@Override
public String toString() {
  return "User";
}

// 使用 Lombok 简化代码（推荐）
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
  private String id;
  private String name;
}

// 使用 @Autowired 或构造函数注入
@Service
public class DoctorService {
  private final DoctorRepository doctorRepository;
  
  @Autowired
  public DoctorService(DoctorRepository doctorRepository) {
    this.doctorRepository = doctorRepository;
  }
}
```

#### JPA 实体规范
```java
@Entity
@Table(name = "doctors")
public class Doctor {
  @Id
  private String id;
  
  @Column(unique = true, nullable = false)
  private String username;
  
  @JsonIgnore
  @Column(nullable = false)
  private String password;
  
  @Column(name = "is_active")
  private Boolean isActive = true;
  
  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;
  
  // 构造器
  public Doctor() {}
  
  // Getter/Setter
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  
  // @PrePersist 和 @PreUpdate
  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }
}
```

## 通用模式

### 错误处理

#### TypeScript/前端
```typescript
// 使用 try-catch 处理预期错误
try {
  const result = await apiCall();
} catch (error) {
  // 处理特定错误类型
  if (error instanceof NetworkError) {
    // 重试逻辑
    message.error('网络错误，请重试');
  } else if (error instanceof ValidationError) {
    // 显示用户消息
    message.error(error.message);
  }
}

// 不要吞掉异常
// 错误
try {
  riskyOperation();
} catch (e) {
  // 空 catch 块
}

// 正确
try {
  riskyOperation();
} catch (e) {
  console.error('Operation failed:', e);
  message.error('操作失败，请重试');
}
```

#### Java/后端
```java
// 使用 try-catch 处理异常
@PostMapping("/register")
public ResponseEntity<Map<String, Object>> register(
    @RequestBody RegisterRequest request) {
  Map<String, Object> response = new HashMap<>();
  
  try {
    Patient patient = patientService.register(
      request.getUsername(),
      request.getPassword(),
      // ...
    );
    
    response.put("code", 200);
    response.put("data", patientToMap(patient));
    return ResponseEntity.ok(response);
    
  } catch (RuntimeException e) {
    if (e.getMessage().contains("Username already exists")) {
      response.put("code", 1001);
      response.put("message", "该用户名已被注册");
    } else {
      response.put("code", 1005);
      response.put("message", "系统繁忙，请稍后重试");
    }
    return ResponseEntity.badRequest().body(response);
  }
}

// 使用自定义异常类
public class BusinessException extends RuntimeException {
  private final int code;
  
  public BusinessException(int code, String message) {
    super(message);
    this.code = code;
  }
  
  public int getCode() {
    return code;
  }
}
```

### 日志记录

#### TypeScript/前端
```typescript
// 使用适当的日志级别
console.debug('Detailed debug information');
console.info('General information');
console.warn('Warning message');
console.error('Error occurred');

// 包含上下文信息
console.info('User login', { userId: user.id });

// 不要记录敏感信息
// 错误
console.info('User authenticated', { password: user.password });

// 正确
console.info('User authenticated', { userId: user.id });
```

#### Java/后端
```java
// 使用 SLF4J + Logback
private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

// 使用适当的日志级别
logger.debug("Detailed debug information");
logger.info("User {} logged in", username);
logger.warn("Warning message");
logger.error("Error occurred", e);

// 使用占位符而非字符串拼接
// 错误
logger.info("User " + username + " logged in");

// 正确
logger.info("User {} logged in", username);

// 不要记录敏感信息
// 错误
logger.info("User password: {}", user.getPassword());

// 正确
logger.info("User authenticated: {}", user.getUsername());
```

### 注释和文档

#### 何时注释
- 解释"为什么"而不是"是什么"（代码应该自解释）
- 记录复杂算法或业务逻辑
- 注意变通方法或临时解决方案
- 记录公共 API 和接口

#### 注释风格

##### TypeScript
```typescript
/**
 * 计算含税总价
 * 
 * @param items - 商品和数量数组
 * @param taxRate - 税率（如 0.08 表示 8%）
 * @returns 含税总价
 */
function calculateTotalWithTax(items: Item[], taxRate: number): number {
  // 计算小计
  const subtotal = items.reduce((sum, item) => 
    sum + (item.price * item.quantity), 0);
  
  // 应用税率
  return subtotal * (1 + taxRate);
}

// 行内注释用于复杂逻辑
// 使用 Dijkstra 算法查找最短路径
const shortestPath = findShortestPath(graph, start, end);
```

##### Java
```java
/**
 * 患者注册
 * 
 * @param request 注册请求对象
 * @return 响应实体包含注册结果
 * @throws BusinessException 当用户名已存在时
 */
@PostMapping("/register")
public ResponseEntity<Map<String, Object>> register(
    @RequestBody RegisterRequest request) {
  // 验证参数
  if (request.getUsername() == null || 
      request.getUsername().trim().isEmpty()) {
    // ...
  }
  
  // 注册患者
  Patient patient = patientService.register(
    request.getUsername(),
    request.getPassword(),
    // ...
  );
  
  return ResponseEntity.ok(response);
}

/**
 * 将 Patient 实体转换为 Map (排除密码)
 */
private Map<String, Object> patientToMap(Patient patient) {
  Map<String, Object> map = new HashMap<>();
  map.put("id", patient.getId());
  map.put("username", patient.getUsername());
  // 不包含密码
  return map;
}
```

## 测试标准

### 测试结构

#### TypeScript/前端（待配置）
```typescript
// 使用 Vitest 或 Jest
describe('DoctorService', () => {
  let doctorService: DoctorService;
  let mockApi: jest.Mocked<typeof api>;
  
  beforeEach(() => {
    mockApi = {
      get: jest.fn(),
      post: jest.fn(),
    };
    doctorService = new DoctorService(mockApi);
  });
  
  describe('getActiveDoctors', () => {
    it('should return active doctors when API call succeeds', async () => {
      // Arrange
      const mockDoctors = [
        { id: 'doc001', name: '张伟医生', isActive: true }
      ];
      mockApi.get.mockResolvedValue({ data: mockDoctors });
      
      // Act
      const result = await doctorService.getActiveDoctors();
      
      // Assert
      expect(result).toEqual(mockDoctors);
      expect(mockApi.get).toHaveBeenCalledWith('/api/doctors/active');
    });
    
    it('should throw error when API call fails', async () => {
      // Arrange
      mockApi.get.mockRejectedValue(new NetworkError());
      
      // Act & Assert
      await expect(doctorService.getActiveDoctors())
        .rejects.toThrow(NetworkError);
    });
  });
});
```

#### Java/后端
```java
@SpringBootTest
class DoctorServiceTest {
  @MockBean
  private DoctorRepository doctorRepository;
  
  @Autowired
  private DoctorService doctorService;
  
  @Test
  void shouldReturnActiveDoctorsWhenCalled() {
    // Arrange
    Doctor doctor = new Doctor();
    doctor.setId("doc001");
    doctor.setName("张伟医生");
    doctor.setIsActive(true);
    
    when(doctorRepository.findByIsActiveTrue())
      .thenReturn(Arrays.asList(doctor));
    
    // Act
    List<Doctor> result = doctorService.getActiveDoctors();
    
    // Assert
    assertEquals(1, result.size());
    assertEquals("doc001", result.get(0).getId());
    verify(doctorRepository).findByIsActiveTrue();
  }
  
  @Test
  void shouldThrowExceptionWhenDoctorNotFound() {
    // Arrange
    when(doctorRepository.findById("doc999"))
      .thenReturn(Optional.empty());
    
    // Act & Assert
    assertThrows(DoctorNotFoundException.class, () -> {
      doctorService.getDoctorById("doc999");
    });
  }
}
```

### 测试命名
- 使用描述性测试名称
- 遵循模式: `should [expected behavior] when [condition]`
- 示例: `should return user when valid ID is provided`

## 代码审查指南

### 审查要点
1. **功能性**: 代码是否按预期工作？
2. **可读性**: 代码是否易于理解？
3. **测试**: 是否有足够的测试？
4. **性能**: 是否有性能问题？
5. **安全性**: 是否有安全漏洞？
6. **可维护性**: 此代码是否易于维护？

### 审查注释
- 建设性和具体性
- 提出建议，而不仅仅是批评
- 关注代码，而不是人
- 使用"我们"语言: "我们可以考虑..."

## 工具配置

### ESLint (TypeScript/Vue)
```json
// .eslintrc.json
{
  "extends": [
    "eslint:recommended",
    "plugin:@typescript-eslint/recommended",
    "plugin:vue/vue3-recommended"
  ],
  "rules": {
    "no-console": ["warn", { "allow": ["warn", "error"] }],
    "@typescript-eslint/explicit-function-return-type": "error",
    "@typescript-eslint/no-unused-vars": ["error", { "argsIgnorePattern": "^_" }],
    "vue/multi-word-component-names": "off"
  }
}
```

### Prettier (TypeScript/Vue)
```json
// .prettierrc.json
{
  "semi": true,
  "singleQuote": true,
  "tabWidth": 2,
  "trailingComma": "es5",
  "printWidth": 100,
  "vueIndentScriptAndStyle": true
}
```

### Maven Checkstyle (Java)（待配置）
```xml
<!-- pom.xml -->
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-checkstyle-plugin</artifactId>
  <version>3.3.0</version>
  <configuration>
    <configLocation>checkstyle.xml</configLocation>
    <failOnViolation>true</failOnViolation>
  </configuration>
  <executions>
    <execution>
      <phase>validate</phase>
      <goals>
        <goal>check</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

## 持续集成

### 预提交钩子（待配置）
- 运行 linter 和 formatter
- 运行单元测试
- 检查安全漏洞
- 验证提交消息格式

### CI 流水线（待配置）
1. Lint 和格式检查
2. 单元测试
3. 集成测试
4. 构建验证
5. 安全扫描
6. 部署到测试环境

## 项目特定约定

### API 响应格式
```json
{
  "code": 200,
  "data": { ... },
  "message": "success"
}
```

### 错误码定义
| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 1001 | 用户名已存在 |
| 1002 | 用户名或密码错误 |
| 1003 | 用户不存在 |
| 1004 | 必填参数为空 |
| 1005 | 系统错误 |

### 国际化键命名
```typescript
// 使用嵌套结构
t('home.hero.title')
t('home.hero.subtitle')
t('header.login')
t('header.logout')

// 对应 locales 文件结构
export default {
  home: {
    hero: {
      title: '...',
      subtitle: '...'
    }
  },
  header: {
    login: '登录',
    logout: '登出'
  }
};
```

## 相关源码文件

| 文件 | 路径 | 说明 |
|------|------|------|
| AuthController.java | `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/controller/AuthController.java` | 认证控制器（代码示例） |
| DoctorController.java | `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/controller/DoctorController.java` | 医生控制器 |
| Doctor.java | `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/entity/Doctor.java` | 医生实体类 |
| Patient.java | `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/entity/Patient.java` | 患者实体类 |
| Home.vue | `web/qa-web/src/views/Home.vue` | 首页组件（代码示例） |
| AppHeader.vue | `web/qa-web/src/components/AppHeader.vue` | 头部组件 |
| auth.ts | `web/qa-web/src/api/auth.ts` | 认证 API |
| store/index.ts | `web/qa-web/src/store/index.ts` | Pinia Store |

---

*这些编码标准应根据特定项目需求和团队偏好进行调整。鼓励定期审查和更新此文档。*
