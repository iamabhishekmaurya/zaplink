import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { BrowserModule } from './browser/browser.module';
import { MetadataModule } from './metadata/metadata.module';
import { HealthModule } from './health/health.module';

@Module({
  imports: [BrowserModule, MetadataModule, HealthModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
