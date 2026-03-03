import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.setGlobalPrefix('api/scraper');
  await app.listen(8095);
  console.log(`Scraper service is running on: http://localhost:8095/api/scraper`);
}
bootstrap();
